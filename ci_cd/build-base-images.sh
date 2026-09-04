#!/bin/bash
#
# Rebuilds all base images

set -euo pipefail

# Move to script dir
cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

# Load conversion table
source ./conversion_table.sh

function print-usage() {
	cat <<EOF
Usage: $0 [options]

Rebuild all Banner base images

Understood options:
  --[no-]build         Enable or disable actually building the images. Defaults to on. If off, will print what would be run instead.
  --[no-]push          Enable or disable the pushing of built images. Defaults to on. If off, will print what would be run instead.
  --[no-]pull          Enable or disable the pulling of base images from Docker Hub. Defaults to on.
  --[no-]cache         Enable or disable the Docker build cache. Defaults to off.
  --dry-run            Equivalent to --no-build --no-push
	--docker-cmd=<CMD>   Override the command used for Docker commands. Can provide an alternative implementation (e.g. podman) or point to a binary not in \$PATH. Must provide a Docker-compatible build and push CLI.
  --help               Display this help message and exit.
EOF
}

build=1
push=1
pull=1
cache=0
docker_cmd=docker

date="$(date +%Y%m%d-%H%M%S)"

while [ $# -gt 0 ]; do
	case $1 in
	--build)
		build=1
		;;
	--push)
		push=1
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
	--no-pull)
		pull=0
		;;
	--no-cache)
		cache=0
		;;
	--dry-run)
		build=0
		push=0
		;;
	--docker-cmd=*)
		docker_cmd="${1#--docker-cmd=}"
		;;
	--docker-cmd)
		docker_cmd="$2"
		shift
		;;
	--help)
		print-usage
		exit 0
		;;
	*)
		echo "Error: this script takes no positional arguments. Unexpected argument: $1"
		print-usage >&2
		exit 1
		;;
	esac
	shift
done

stage=prep

function docker {
	case $stage in
	prep)
		echo "WARN: Docker command running in prep stage! This is unexpected!" >&2
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

# CD to repo base
cd ..

# Collect all base image dirs. base is first.
base_dirs=(base base-*)

# Build them one at a time
for dir in "${base_dirs[@]}"; do
	stage="build-${dir}"
	tag_date=1
	case "$dir" in
	base-build)
		tag="build-jdk21-latest"
		tag_date=0
		;;
	base*)
		# Remove base prefix
		base_for="${dir#base}"
		# Remove dashes
		base_for="${base_for//-/}"
		tag="9-jdk21-tomcat10${base_for:+-$base_for}"
		;;
	*)
		echo "wtf is this??? $dir"
		exit 67
		;;
	esac
	echo "Building ${dir}, tagged ${tag}"
	docker build "${dir}" --tag="usuit/banner-base:${tag}" $([[ $tag_date -eq 1 ]] && echo --tag=usuit/banner-base:$tag-$date) $([[ $cache -eq 0 ]] && echo --no-cache)
	stage="push-${dir}"
	docker push "usuit/banner-base:${tag}"
	[[ $tag_date -eq 1 ]] && docker push "usuit/banner-base:${tag}-${date}"
done
