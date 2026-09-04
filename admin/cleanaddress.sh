#!/bin/bash
# Cleanaddress modification script. Should be installed to and run from build stage.
# Expects the following environment:
# - ENVIRONMENT -- Banner environment
# These should be provided when run in the Docker build stage during a full build.

set -e

exec >&2

case "${ENVIRONMENT,,}" in
zdevl | zprod)
	echo "Building for ${ENVIRONMENT^^}. Configuring for clean address plugin."
	;;
*)
	echo "Building for ${ENVIRONMENT^^}. Clean address not needed, nothing to do."
	;;
esac

config_file="BannerAdmin.ws/WEB-INF/classes/config.properties"

if grep -iq "plugins *= *com.runnertech.ext" $config_path; then
	echo "Plugin definition already exists, nothing to do."
else
	cat - "$config_file" <<EOF >tmp
#####################
# Plug-in Definition
#####################

plugins = com.runnertech.ext

EOF
	mv tmp "$config_file"
fi
