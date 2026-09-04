#!/bin/bash
# Extra runtime property manglement required for banner admin

ORIGINAL_PROPFILE="$PROPFILE"

PROPFILE=/usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties

# Load SAML keystore password
read keystore_password </saml/keystore/password || :

setProperty saml.keystore.password "${keystore_password}"
setProperty saml.sign.key.password "${keystore_password}"

PROPFILE="$ORIGINAL_PROPFILE"
