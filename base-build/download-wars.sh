#!/bin/bash
# Usage: download-wars [--build-dir <dir>] <instance> [<file> ...]
#
# Download WAR files from build.banner and unpack them in the build dir.
#   dir      -- The build dir to use. If unset, defaults to the value of $BUILD_DIR, or the current directory if unspecified.
#   instance -- The Banner instance to download files for. All files will be fetched from /u01/deploy/<instance>/self-service/
#   file     -- The WAR file to download and unpack, WITHOUT the file extension (.war is implied and will be appended).
#
# WAR files will be downloaded and extracted into directories sharing names with the files, minus the extension. The WARs will then be deleted.
# If standard output is not a tty, prints paths to the directories that are extracted one per line.
set -e

declare -a positionals

echo "got args $*" >&2

idx=0

while [ $# -gt 0 ]; do
	case "$1" in
		--build-dir)
			BUILD_DIR="${2}"
			# option with argument, need to shift twice
			shift
			;;
		--build-dir=*)
			BUILD_DIR="${1/--build-dir=/}"
			;;
		*)
			positionals[$idx]="$1"
			let idx=idx+1
			;;
	esac
	shift
done

# get the build dir
BUILD_DIR="${BUILD_DIR:-$PWD}"
cd "${BUILD_DIR}"


if [ "${#positionals[@]}" -lt 1 ]; then
	echo "Usage: $0 [--build-dir <dir>] <instance> [<file> ...]" >&2
	exit 1
fi

instance="${positionals[0]}"
# Get filenames
files=("${positionals[@]:1}")

echo "Downloading WARs ${files[*]} for ${instance^^} from build.banner" >&2

# Download and extract each file
for file in "${files[@]}"; do
	echo "Downloading ${file}.war for ${instance^^} from build.banner" >&2
	scp "build.banner:/u01/deploy/${instance}/self-service/${file}.war" . >&2
	mkdir -p "${file}"
	cd "${file}"
	jar xvf "../${file}.war" >&2
	# Create symlink for saml data
	ln -sf /saml saml
	cd ..
	# print directory name to stdout if stdout is not a tty.
	[ -t 1 ] || echo "${BUILD_DIR}/${file}"
	rm "${file}.war"
done
