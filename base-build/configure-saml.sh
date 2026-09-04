#!/bin/bash
#
# Usage: configure-saml <APP_NAME>
#
# Configure admin apps (those with an ${APP_NAME}.ws tomcat web app) for SAML
#
# Expects the following environment:
#   ENVIRONMENT    -- the Banner environment we're building for
#
# Assumes the ${APP_NAME}.ws tree is located at ./${APP_NAME}.ws. If it is not, the script will fail.

set -euo pipefail

APP_NAME="$1"

case $APP_NAME in
BannerAdmin)
	short_name="adminpages"
	;;
BannerAccessMgmt)
	short_name="bam"
	;;
*)
	echo "Error: not an admin app! SAML should be configured using the app's .groovy file, preferably mounted as runtime configuration!" >&2
	exit 1
	;;
esac

if ! [[ -d "./${APP_NAME}.ws" ]]; then
	echo "Error: could not find ${APP_NAME}.ws tree" >&2
	exit 1
fi

classes_dir="$(pwd)/${APP_NAME}.ws/WEB-INF/classes"
propfile="${classes_dir}/config.properties"

echo "Configuring ${APP_NAME} for SAML login..."

set-property() {
	local prop="$1" val="$2"

	if [ $(grep -c "$prop" "$propfile") -eq 0 ]; then
		echo "${prop}=$val" >>"$propfile"
	else
		sed -i "s/$prop *=.*/$prop=${val////\\/}/" "$propfile"
	fi
}

ln -s /saml/metadata/sp.xml "${classes_dir}/saml-sp.xml"
ln -s /saml/metadata/idp.xml "${classes_dir}/saml-idp.xml"

set-property saml.keystore "file:///saml/keystore/keystore.jks"
set-property saml.sign.key.alias "${ENVIRONMENT}-${short_name}-sp"
set-property saml.sp.metadata.filename "saml-sp.xml"
set-property saml.idp.metadata.filename "saml-idp.xml"

echo "SAML metaconfiguration applied. SAML metadata must be provided at /saml/metadata/{sp,idp}.xml."
