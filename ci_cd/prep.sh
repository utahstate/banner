#!/bin/bash

set -euo pipefail

# Move to script dir if we weren't called from it
cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

# Load the conversion table
source ./conversion_table.sh

source ./base-image-utils.sh

function print-usage() {
	cat <<EOF
Usage: $0 [options] <app-name> <version> <instance>

app-name:    Banner application name
version:     Banner application version
instance:    Banner deployment instance we are building for (one of ZPROD, ZDEVL, ZPPRD, or ZLDTST)

Understood options:
  --[no-]build                 Enable or disable the building of new images. Defaults to on.
  --[no-]push                  Enable or disable the pushing of built images. Defaults to on.
  --[no-]deploy                Enable or disable the deployment of built images to Kubernetes. Defaults to on.
  --[no-]pull                  Enable or disable the pulling of base images from Docker Hub. Defaults to on.
  --[no-]cache                 Enable or disable the use of the Docker build cache. Defaults to off.
  --dry-run                    Request a dry run of the build process. Shorthand for --no-build --no-push --no-deploy.
  --docker-cmd=<CMD>           Override the command used for Docker commands. Can provide an alternative implementation (e.g. podman) or point to a binary not in \$PATH. Must provide a Docker-compatible build and push CLI.
  --zip-password=<PASSWORD>    Set the password used for the cleanaddress patch ZIP file. If none are specified, defaults to "transcript".
  --ssh-key=<PATH_TO_KEY>      Set the SSH key used to authenticate to the Banner build server for downloading app archives. Ignored if --use-existing-agent is specified. Defaults to ~/.ssh/id_ed25519.
  --use-existing-agent         Disable the spawning of a new SSH agent, and instead use an already-running agent. Builds will fail if no agent is available.
  --help                       Display this help message and exit.
EOF
}

build=1
push=1
deploy=1
pull=1
cache=0
spawn_agent=1
docker_cmd=docker
ssh_key="${SSH_KEY:-$HOME/.ssh/id_ed25519}"
zip_password=transcript

date=$(date +%Y%m%d-%H%M%S)

param_parsers=('app_name="$1"' 'version="$1"' 'instance="$1"')

while [ $# -gt 0 ]; do
	case $1 in
	--build)
		build=1
		;;
	--push)
		push=1
		;;
	--deploy)
		deploy=1
		;;
	--pull)
		pull=1
		;;
	--cache)
		cache=1
		;;
	--no-build)
		build=0
		;;
	--no-push)
		push=0
		;;
	--no-deploy)
		deploy=0
		;;
	--no-pull)
		pull=0
		;;
	--no-cache)
		cache=0
		;;
	--dry-run)
		build=0
		push=0
		deploy=0
		;;
	--docker-cmd=*)
		docker_cmd="${1#--docker-cmd=}"
		;;
	--docker-cmd)
		docker_cmd="$2"
		shift
		;;
	--zip-password=*)
		zip_password="${1#--zip-password=}"
		;;
	--zip-password)
		zip_password="$2"
		shift
		;;
	--ssh-key=*)
		ssh_key="${1#--ssh-key=}"
		;;
	--ssh-key)
		ssh_key="${2}"
		shift
		;;
	--use-existing-agent)
		spawn_agent=0
		;;
	--help)
		print-usage
		exit 0
		;;
	*)
		if [[ "${#param_parsers[@]}" -eq 0 ]]; then
			echo "Error: unexpected argument: $1"
			exit 1
		fi
		eval "${param_parsers[0]}"
		param_parsers=("${param_parsers[@]:1}")
		;;
	esac
	shift
done

if [[ ${#param_parsers[@]} -ne 0 ]]; then
	echo "Error: missing required arguments." >&2
	print-usage
	exit 1
fi

exec {journal}> >(systemd-cat -t banner-${instance}-build-log)
echo " --- START OF BUILD LOG --- " >&${journal}
echo " > Building app ${app_name}, version ${version}, for instance ${instance}" >&${journal}

function log-tee {
	while read line; do
		echo $line >&${1:-1}
		echo $line >&${journal}
	done
}
exec > >(log-tee) 2> >(log-tee 2)

stage=prep

function docker {
	case $stage in
	prep)
		if [ $pull -eq 0 ]; then
			echo "WOULD RUN> docker $@"
			return
		fi
		;;
	build*)
		if [ $build -eq 0 ]; then
			echo "WOULD RUN> docker $@"
			return
		fi
		;;
	push*)
		if [ $push -eq 0 ]; then
			echo "WOULD RUN> docker $@"
			return
		fi
		;;

	*)
		echo "WARN: Docker command running in ${stage} stage! This is unexpected! --dry-run may not work as intended!" >&2
		;;
	esac
	command "${docker_cmd}" "$@"
}

function kubectl {
	if [[ $stage == deploy ]] && [ $deploy -eq 0 ]; then
		echo "WOULD RUN> kubectl $@"
		return
	fi
	if [[ $stage != deploy ]]; then
		echo "WARN: kubectl command running in ${stage} stage! This is unexpected! --dry-run may not work as intended!" >&2
	fi
	command kubectl "$@"
}

if [ $spawn_agent -eq 1 ]; then
	# Spawn a clean ssh-agent which will be forwarded to the Docker build environment
	eval $(ssh-agent)
	if [ -f "$ssh_key" ]; then
		ssh-add "$ssh_key"
		trap 'eval $(ssh-agent -k)' EXIT
	else
		echo "ERR: SSH key ${ssh_key} does not exist! Will not be able to pull WAR files!" >&2
		exit 1
	fi
fi

ctx_dir="${APP_MAPPING[${app_name,,}]}"
if [[ "${ctx_dir+x}" == "" ]]; then
	echo "ERR: Unknown Banner app ${app_name} -- we have no build instructions!"
	exit 1
fi
if ! [[ -d "../${ctx_dir}" ]]; then
	echo "ERR: No build context available for Banner app ${app_name} (should be at ../${ctx_dir})! Are we in the right directory?"
	exit 1
fi
echo "Build context located at ../${ctx_dir}"

echo "Identifying base image..."
# Coordination lock; if multiple builds are running simultaneously, only one build should be running this check at once so we only rebuild images once.
exec {lock}>./.base-check.lck
flock $lock

# If the base build image is too old, rebuild it.
docker pull docker.io/usuit/banner-base:build-jdk21-latest
if needs-rebuild usuit/banner-base:build-jdk21-latest; then
	echo "Base build image is too old, rebuilding it..."
	rebuild-base ../base-build usuit/banner-base:build-jdk21-latest
fi

generate-reverse-image-table base_images
inspecting="$(get-container-base ../${ctx_dir})"
our_bases=()
# Get all usuit/banner-base images that this app is derived from, and assemble them in reverse order
while [[ "${inspecting}" =~ ^usuit/banner-base: ]]; do
	our_bases=("$inspecting" "${our_bases[@]}")
	inspecting="$(get-container-base "../${base_images[$inspecting]}")"
done

# Check all the collected base images to ensure they were built recently enough.
while [ ${#our_bases[@]} -gt 0 ]; do
	docker pull "docker.io/${our_bases[0]}"
	if needs-rebuild ${our_bases[0]}; then
		# This image needs to be rebuilt, and subsequently all base images derived from it.
		echo -n "Base image ${base_images[${our_bases[0]}]} is too old, rebuilding it"
		[[ ${#our_bases[@]} -gt 1 ]] && echo -n " and all derived bases"
		echo ...
		for image in "${our_bases[@]}"; do
			rebuild-base "${base_images[${image}]}" "${image}"
		done
		break
	fi
	our_bases=("${our_bases[@]:1}")
done

# We've done our work, close the lock descriptor
exec {lock}>&-
echo "Beginning container build for ${app_name} version ${version} in environment ${instance}..."

stage=build
echo "Beginning build..."

image_tag="docker.io/usuit/banner9-${ctx_dir}:${version}-${instance,,}"

cleanaddress_password="${zip_password}" docker build "../${ctx_dir}" $([ $cache -eq 0 ] && echo --no-cache) $([ $pull -eq 1 ] && echo --pull) --platform linux/amd64 -t "${image_tag}" -t "${image_tag}-${date}" --build-arg "VERSION=${version}" --build-arg "ENVIRONMENT=${instance,,}" --secret id=cleanaddress_password --ssh default

stage=push
echo "Build complete, uploading..."

docker push "${image_tag}"
docker push "${image_tag}-${date}"

echo "Push complete, updating deployments..."

stage=deploy
kubectl set image "deployment/${ctx_dir,,}" "app=${image_tag}-${date}" --context "$instance"
target_replicas=$(kubectl get deployment "${ctx_dir,,}" -o json --context "$instance" | jq '.metadata.annotations["banner.usu.edu/desired-replicas"]' -r)
echo -e "Scaling to ${target_replicas} replica"
[ ${target_replicas} -ne 1 ] && echo -e "s"
echo ...
kubectl scale deployment "${ctx_dir,,}" --replicas="${target_replicas}" --context "$instance"

mkdir -p "../${ctx_dir}/.latest-versions/"
echo "${version}" >"../${ctx_dir}/.latest-versions/${instance,,}"
