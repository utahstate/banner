#!/bin/bash

function get-base-images {
	# Print the base images to stdout, one per line. Context dir in field 1, unqualified repo and tag in field 2, with space as field separator.
	local -a base_dirs=($(
		cd -- "$(dirname -- "${BASH_SOURCE[0]:-.}")/../"
		echo base base-*
	))
	local base_for

	for dir in "${base_dirs[@]}"; do
		echo -n "${dir} usuit/banner-base:"
		case "$dir" in
		base-build)
			echo "build-jdk21-latest"
			;;
		base*)
			base_for="${dir#base}"
			base_for="${base_for//-/}"
			echo "9-jdk21-tomcat10${base_for:+-$base_for}"
			;;
		esac
	done
}

function generate-base-image-table {
	# Generate a hash table of base images mapping ctx_dir to image tag. If given an argument, uses that argument as the variable name, otherwise uses 'base_images'.
	local table="${1:-base_images}"
	eval declare -gA -- "${table}"
	while read dir tag; do
		eval ${table}[$dir]=$tag
	done < <(get-base-images)
}

function generate-reverse-image-table {
	# Generate a hash table of base images mapping image tag to ctx_dir. If given an argument, uses that argument as the variable name, otherwise uses 'reverse_base_images'
	local table="${1:-reverse_base_images}"
	eval declare -gA -- "${table}"
	while read dir tag; do
		eval ${table}[$tag]=$dir
	done < <(get-base-images)
}

function get-container-base {
	# Print the final base image used by the given container. Requires grep to support the -P flag.
	grep -Po "(?<=FROM )[^ ]+" "$1/Dockerfile" | tail -n 1
}

function needs-rebuild {
	# Check if the given image was built within the given timespan in seconds (defaults to 1 week).
	# usage: check-age <image> [<max_age_in_seconds>]
	local timespan=${2:-$((3600 * 168))}
	local created=$(date --date="$(docker image inspect "$1" | jq '.[].Created' -r)" +%s)
	((created + timespan < $(date +%s)))
}

function rebuild-base {
	# Rebuilds the given base image. Usage: rebuild-base <context-dir> <tag>

	if [[ ${stage+x} == '' ]]; then
		# Don't leak the stage variable if it doesn't already exist
		local stage
	fi
	local original_stage=${stage:-}
	stage=build-base
	docker build "$1" -t "$2" --no-cache
	stage=push-base
	docker push "$2"
	stage="${original_stage}"
}
