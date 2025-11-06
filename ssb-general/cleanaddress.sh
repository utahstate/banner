#!/bin/bash
# Cleanaddress modification script. Should be installed to and run from build stage.
# Expects the following environment:
# - instance -- Banner instance
# - version  -- App version
# - PASSWORD -- Password for the downloaded archive
# These should be provided when run in the Docker build stage during a full build.

set -e

exec >&2

if [[ "${instance,,}" -eq "zdevl" ]] || [[ "${instance,,}" -eq "zprod" ]]; then
	echo "Building for ${instance^^}. Installing clean address plugin."
else
	echo "Building for ${instance^^}. Clean address not needed, nothing to do."
	exit
fi

copy_err () {
	exit_code=$?
	echo "Failed to copy file ${file}, aborting build..."
	exit ${exit_code}
}

append_version() {
	# rename a file from path/to/example.ext to path/to/example-${version}.ext. Write the remap to the manifest open in $manifest_fd.
	echo "# CLEAN_Address STARTS HERE" >&${manifest_fd}
	for file in "$@"; do
		file_remap="${file%.*}-${version}.${file##*.}"
		cp -v "${file}" "${file_remap}" || copy_err "${file}"
		echo "${file}=${file_remap}" >&${manifest_fd}
	done
	echo "# CLEAN_Address ENDS HERE" >&${manifest_fd}
}

startdir="${PWD}"
cleanaddressdir="${PWD}/clnbannerssb_${version}"
curl -o "${cleanaddressdir}.zip" "https://files.runneredq.com/integrations/RunnerEDQ-Banner9SSB/clnbannerssb_${version}.zip"
unzip -P "${PASSWORD}" "${cleanaddressdir}.zip" -d "${cleanaddressdir}"

cd "${cleanaddressdir}/clnaddr_banner_ssb/assets"

# Open manifest file
exec {manifest_fd}>>"${startdir}/BannerGeneralSsb/assets/manifest.properties"
# Add version number to files and remap them in the manifest
append_version modules/personalInformationApp-mf.js \
							 modules/pi-application-mf.js \
							 personalInformationApp/piEmergencyContact/piEditEmergencyContact-controller.js \
							 personalInformationApp/piEmergencyContact/cleanAddressEmergency-controller.js \
							 personalInformationApp/piEmergencyContact/piEditEmergencyContact.html \
							 personalInformationApp/common/services/cleanAddress-service.js \
							 personalInformationApp/piAddress/piEditAddress-controller.js \
							 personalInformationApp/piAddress/cleanAddressMain-controller.js \
							 personalInformationApp/piAddress/piEditAddress.html
# Close manifest file
exec {manifest_fd}>&-

# Update webapp tree with modified files
cd "${startdir}"
echo "Updating tree with integration files..."
cp -rv "${cleanaddressdir}/clnaddr_banner_ssb/assets" BannerGeneralSsb/
cp -rv "${cleanaddressdir}/clnaddr_banner_ssb/WEB-INF" BannerGeneralSsb/
