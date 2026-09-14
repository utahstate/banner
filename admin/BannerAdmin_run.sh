#!/bin/bash
# Extra runtime property manglement required for banner admin

ORIGINAL_PROPFILE="$PROPFILE"

PROPFILE=/usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties

# Load SAML keystore password
read keystore_password </saml/keystore/password || :

setProperty saml.keystore.password "${keystore_password}"
setProperty saml.sign.key.password "${keystore_password}"

case "${ENVIRONMENT,,}" in
zprod)
	SUBDOMAIN="${SUBDOMAIN:-admin}"
	;;
*)
	SUBDOMAIN="${SUBDOMAIN:-admin-${ENVIRONMENT,,}}"
	;;
esac

# Fix hostname
setProperty webapp.location 'https://'$SUBDOMAIN'.banner.usu.edu/${webapp.context}'
setProperty webapp.wrksp.location 'https://'$SUBDOMAIN'.banner.usu.edu/${webapp.wrksp.context}'

# Fix appnav
sed -i 's|\(<param name="APPNAV_API_URL".*value="\)https://[^\.]*|\1https://'$SUBDOMAIN'|g' /usr/local/tomcat/webapps/"$APP_NAME"/config.xml

# Set theme
if [[ "${THEME_URL+x}" == x ]]; then
	sed -i '/<param name="APP_CSS_URL.*/d;/<param name="APP_CSS_APPEND.*"/d' /usr/local/tomcat/webapps/"$APP_NAME"/config.xml
	sed -i "2i <param name=\"APP_CSS_URL\" value=\"$THEME_URL\" />\\n<param name=\"APP_CSS_APPEND\" value=\"true\" />" /usr/local/tomcat/webapps/"$APP_NAME"/config.xml
fi

PROPFILE="$ORIGINAL_PROPFILE"
