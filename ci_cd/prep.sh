#!/bin/bash

set -euo pipefail

# Hash table mapping lowercased "official" app names to the name of the directory which holds that app's build context
declare -A APP_MAPPING

APP_MAPPING[applicationnavigator]="application-navigator"
APP_MAPPING[banneraccessmgmt]="access-management"
APP_MAPPING[banneradmin]="admin"
APP_MAPPING[bannereventpublisher]="event-publisher"
APP_MAPPING[bannerextensibility]="extensibility"
APP_MAPPING[bannergeneralssb]="ssb-general"
APP_MAPPING[brim]="brim"
APP_MAPPING[businessprocessapi]="bpapi"
APP_MAPPING[communicationmanagement]="communicationmanagement"
APP_MAPPING[documentmanagementapi]="api-documentmanagement"
APP_MAPPING[employeeselfservice]="ss-employee"
APP_MAPPING[etranscriptapi]="api-etranscript"
APP_MAPPING[facultyselfservice]="ss-faculty"
APP_MAPPING[financeselfservice]="ss-finance"
APP_MAPPING[integrationapi]="api-integration"
APP_MAPPING[studentapi]="api-student"
APP_MAPPING[studentregistrationssb]="ssb-registration"
APP_MAPPING[studentselfservice]="ss-student"

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
  --dry-run                    Request a dry run of the build process. Shorthand for --no-build --no-push --no-deploy.
  --docker-cmd=<CMD>           Override the command used for Docker commands. Can provide an alternative implementation (e.g. podman) or point to a binary not in \$PATH.
  --zip-password=<PASSWORD>    Set the password used for the cleanaddress patch ZIP file. If none are specified, defaults to "transcript".
  --ssh-key=<PATH_TO_KEY>      Set the SSH key used to authenticate to the Banner build server for downloading app archives. Required if not using an existing SSH agent.
  --use-existing-agent         Disable the spawning of a new SSH agent, and instead use an already-running agent. Builds will fail if no agent is available.
  --help                       Display this help message and exit.
EOF
}

build=1
push=1
deploy=1
spawn_agent=1
docker_cmd=docker
ssh_key=

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
	--no-build)
		build=0
		;;
	--no-push)
		push=0
		;;
	--no-deploy)
		deploy=0
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

stage=prep

function docker {
	case $stage in
	prep)
		echo "WARN: Docker command running in prep stage! This is unexpected!" >&2
		;;
	build)
		if [ $build -eq 0 ]; then
			echo "WOULD RUN> docker $@"
			return
		fi
		;;
	push)
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
		trap "ssh-agent -k" EXIT
	else
		echo "ERR: SSH key ${ssh_key} does not exist! Will not be able to pull WAR files!" >&2
		exit 1
	fi
fi

echo "Beginning container build for ${app_name} version ${version} in environment ${instance}..."
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

cd "../${ctx_dir}"

cleanaddress_password="${zip_password}" docker build --pull --platform linux/amd64 -t "usu/banner9-${ctx_dir}:${instance,,}-${version}-${date}" --build-arg "version=${version}" --build-arg "instance=${instance^^}" --secret id=cleanaddress_password --ssh
