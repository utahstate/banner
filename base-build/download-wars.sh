#!/bin/bash
# Usage: download-wars [--build-dir <dir>] <instance> [<file> ...]
#
# Download WAR files from build.banner and unpack them in the build dir.
#   dir      -- The build dir to use. If unset, defaults to the value of $BUILD_DIR, or the current directory if $BUILD_DIR is unset.
#   instance -- The instance we are using.
#   file     -- The WAR file to download and unpack, WITHOUT the file extension (.war is implied and will be appended).
#
# WAR files will be downloaded and extracted into directories sharing names with the files, minus the extension. The WARs will then be deleted.
# If standard output is not a tty, prints paths to the directories that are extracted one per line.
set -e

exec {stdout}>&1 1>&2

echo "got args $*"

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
		if [[ ${instance+x} == x ]]; then
			break
		else
			instance="$1"
		fi
		;;
	esac
	shift
done

# get the build dir
BUILD_DIR="${BUILD_DIR:-$PWD}"
cd "${BUILD_DIR}"

if [[ ${instance+x} == '' ]]; then
	echo "Error: no instance given, cannot operate"
	exit 1
fi

if [ $# -gt 1 ]; then
	echo "Downloading WARs for ${instance^^} from build.banner"
elif [ $# -eq 0 ]; then
	echo "No WARs given for download, ignoring..."
fi

# Download and extract each file
for file in "$@"; do
	echo "Downloading ${file}.war for ${instance^^} from build.banner"
	scp "build.banner.usu.edu:/u01/deploy/${instance,,}/self-service/${file}.war" .
	mkdir -p "${file}"
	cd "${file}"
	jar xvf "../${file}.war"
	# Create symlink for saml data
	ln -sf /u01/saml saml
	cd ..
	# print directory name to stdout if stdout is not a tty.
	[ -t 1 ] || echo "${BUILD_DIR}/${file}" >&${stdout}
	rm "${file}.war"
done
