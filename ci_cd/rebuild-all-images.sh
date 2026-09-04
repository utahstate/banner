#!/bin/bash
#
# Rebuilds all images for the given environment using the last-known versions.
#
# Usage: rebuild-all-images.sh <environment> [ARGS]
#
# Environment must be given before any args. All args will be passed through to the build script prep.sh. See 'prep.sh --help' for details.
#

set -euo pipefail
cd -- "$(dirname -- "${BASH_SOURCE[0]}")"
source ./conversion_table.sh

environment="$1"
shift

for their_name in "${!APP_MAPPING[@]}"; do
	our_name="${APP_MAPPING[$their_name]}"
	if ! [[ -f ../${our_name}/.latest-versions/${environment} ]]; then
		echo "WARN: no cached version for ${our_name} (${their_name})! Cannot build!"
		continue
	fi
	version=$(cat ../${our_name}/.latest-versions/${environment})
	if [[ "${version##-*}" == "" ]] || [[ "${version##*\$*}" == "" ]]; then
		echo "DANGER: Detected possible command injection in version cache file for ${our_name} (${their_name}) located in ../${our_name}/.latest-versions/${environment}! Refusing to build this app!"
		continue
	fi
	echo "INFO: Building ${our_name} (${their_name}) version ${version} for ${environment}"
	./prep.sh "$@" "${their_name}" "$(cat -- ../${our_name}/.latest-versions/${environment})" "${environment}"
done
