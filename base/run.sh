#!/bin/bash
# Thanks to Virginia Tech and College of William and Mary for some of the setup in this file

# Fail fast
set -euo pipefail

# Main Javaprop file to manipulate
PROPFILE="/usr/local/tomcat/conf/catalina.properties"

# Directory for property dropin files
PROPERTY_DROPINS=${PROPERTY_DROPINS:-"/run/app-config/properties.d/"}

if [ ! -f "$PROPFILE" ]; then
	echo "Unable to find properties file $PROPFILE"
	exit 1
fi

admin-setProperty() {
	local prop val
	prop="$1"
	val="$2"

	#Enable Theme for BannerAdmin
	if [ "$prop" = "theme.url" ]; then
		sed -i "\|<param name=\"APP_CSS_URL\.*|d" /usr/local/tomcat/webapps/BannerAdmin/config.xml
		sed -i "\|<param name=\"APP_CSS_APPEND\.*|d" /usr/local/tomcat/webapps/BannerAdmin/config.xml
		sed -i "2i <param name=\"APP_CSS_URL\" value=\"$val\" />\\n<param name=\"APP_CSS_APPEND\" value=\"true\" />" /usr/local/tomcat/webapps/BannerAdmin/config.xml
	fi

	#Set CAS server for BannerAdmin.ws
	if [ "$prop" = "cas.url" ]; then
		sed -i "s|^cas\.server\.location.*|cas\.server\.location = $val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
	fi

	#Set Banner9.baseurl for BannerAdmin.ws and appnav links in BannerAdmin
	if [ "$prop" = "banner9.baseurl" ]; then
		sed -i "s|^webapp\.location.*|webapp\.location = $val\/\${webapp.context}|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
		sed -i "s|^webapp\.wrksp\.location.*|webapp\.wrksp\.location = $val\/\${webapp.wrksp.context}|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
		#sed -i "s|<param name=\"APPNAV_HELP_URL\".*|<param name=\"APPNAV_HELP_URL\"   value=\"$val/bannerHelp/Main?page=\" />|g" /usr/local/tomcat/webapps/BannerAdmin/config.xml
		sed -i "s|<param name=\"APPNAV_API_URL\".*|<param name=\"APPNAV_API_URL\" value=\"$val/applicationNavigator/static/dist/m.js\" />|g" /usr/local/tomcat/webapps/BannerAdmin/config.xml
	fi

	if [ "$prop" = "saml.keystore.env" ]; then
		sed -i "s|^saml\.keystore = .*|saml\.keystore = file://$val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
		cp /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/applicationContext.xml.saml /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/applicationContext.xml
	fi

	if [ "$prop" = "saml.keystore.password.env" ]; then
		sed -i "s|^saml\.keystore\.password.*|saml\.keystore\.password = $val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
	fi

	if [ "$prop" = "saml.sign.key.alias.env" ]; then
		sed -i "s|^saml\.sign\.key\.alias.*|saml\.sign\.key\.alias = $val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
	fi

	if [ "$prop" = "saml.sign.key.password.env" ]; then
		sed -i "s|^saml\.sign\.key\.password.*|saml\.sign\.key\.password = $val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
	fi

	if [ "$prop" = "saml.sp.metadata.filename.env" ]; then
		sed -i "s|^saml\.sp\.metadata\.filename.*|saml\.sp\.metadata\.filename = $val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
	fi

	if [ "$prop" = "saml.idp.metadata.filename.env" ]; then
		sed -i "s|^saml\.idp\.metadata\.filename.*|saml\.idp\.metadata\.filename = $val|g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
	fi
}

ss-setProperty() {
	local prop val
	prop="$1"
	val="$2"

	if [ -f "/usr/local/tomcat/webapps/*/WEB-INF/web.xml" ]; then
		if [ "$prop" = "banner9.baseurl" ]; then
			for settings_file in /usr/local/tomcat/webapps/*/WEB-INF/web.xml; do
				xmlstarlet ed --inplace -N x="http://java.sun.com/xml/ns/javaee" -u "/x:web-app/x:filter[x:filter-name[normalize-space(text())='CAS Validation Filter']]/x:init-param[x:param-name[normalize-space(text())='serverName']]/x:param-value" -v "$val" "$settings_file"
			done
		fi

		if [ "$prop" = "cas.url" ]; then
			for settings_file in /usr/local/tomcat/webapps/*/WEB-INF/web.xml; do
				xmlstarlet ed --inplace -N x="http://java.sun.com/xml/ns/javaee" -u "/x:web-app/x:filter[x:filter-name[normalize-space(text())='CAS Validation Filter']]/x:init-param[x:param-name[normalize-space(text())='casServerUrlPrefix']]/x:param-value" -v "$val" "$settings_file"
			done
		fi
	fi
}

setProperty() {
	local prop="$1" val="$2"

	# call app-type-specific setProperty hooks
	case "${APP_TYPE}" in
	admin | admin-api)
		admin-setProperty "$prop" "$val"
		;;
	self-service | api | bcm)
		ss-setProperty "$prop" "$val"
		;;
	*) ;;
	esac

	if [ $(grep -c "$prop" "$PROPFILE") -eq 0 ]; then
		echo "${prop}=$val" >>"$PROPFILE"
	else
		val=$(echo "$val" | sed 's#/#\\/#g')
		sed -i "s/$prop=.*/### Overridden at startup\n$prop=$val/" "$PROPFILE"
	fi
}

setPropsFromFile() {
	local file=$1 prop val
	echo "### Section transcluded from ${file}" >>"$PROPFILE"
	for l in $(grep '=' "$file" | grep -v '^ *#'); do
		IFS='=' read prop val < <(echo "$l")
		setProperty "$prop" "$val"
	done
}

if [ -d /run/passwords ]; then
	for file in /run/passwords/*; do
		echo "INFO: setting password property for $(basename "$file")"
		setProperty "$(basename "$file").password" "$(cat "$file")"
	done
fi

setPropFromEnvPointingToFile() {
	prop=$1
	val=$2
	[ -z "$val" ] && return
	# If the value is a file, use the contents of that file as the new value
	if [ -f "$val" ]; then
		val=$(cat "$val")
		setProperty "$prop" "$val"
	else
		# If it's not a file, use the value of the variable as the password
		setProperty "$prop" "$val"
	fi
}

# This will silently fail if the environments are not set.
setPropFromEnvPointingToFile banproxy.password "${BANPROXY_PASSWORD:-}"
setPropFromEnvPointingToFile banssuser.password "${BANSSUSER_PASSWORD:-}"
setPropFromEnvPointingToFile commmgr.password "${COMMMGR_PASSWORD:-}"

setPropFromEnv() {
	prop=$1
	val=$2
	# If no value was given, abort
	[ -z "$val" ] && return
	if [ $(grep -c "$prop" $PROPFILE) -eq 0 ]; then
		setProperty "$prop" "$val"
	fi
}

# Merge in all property files (files in $PROPERTY_DROPINS that end in .properties)
for propFile in ${PROPERTY_DROPINS}/*.properties; do
	if [[ "${propFile}" == "${PROPERTY_DROPINS}/*.properties" ]]; then
		echo "WARN: No dropin config files found, did you forget to mount them?" >&2
		break
	fi
	echo "INFO: loading properties from file ${propFile}"
	setPropsFromFile $propFile
done

echo "### BEGIN SECTION: Imported from BANNER_ environment" >>"${PROPFILE}"

# Dynamically scan the environment for BANNER_* environment variables and set their associated properties.
# Any environment variable of the form BANNER_PATH_TO_PROP will be used to set a property of the form "path.to.prop".
for var in "${!BANNER_@}"; do
	# Remove BANNER prefix
	propname="${var/BANNER_/}"
	# Lowercase the string
	propname="${propname,,}"
	# Replace underscores with dots
	propname="${propname//_/.}"
	echo "INFO: Setting property ${propname} from \$${var}"
	# Set the property, overriding anything set in static config
	setProperty "${propname}" "${!var}"
done

declare -a cmd

echo "### BEGIN SECTION: Imported from command line arguments" >>"${PROPFILE}"

# Finally, parse the command line
while [ $# -gt 0 ]; do
	case "$1" in
	--prop=*=*)
		# property declaration, set this property in the propfile
		IFS='=' read opt propname propval < <(echo "$1")
		echo "INFO: setting property ${propname} provided on command line"
		setProperty "${propname}" "${propval}"
		;;
	--prop | -p)
		IFS='=' read propname propval < <(echo "$2")
		echo "INFO: setting property ${propname} provided on command line"
		setProperty "${propname}" "${propval}"
		shift
		;;
	--)
		# done parsing options, everything is now part of the command
		shift
		cmd+=("$@") # Bash will split this into words for each arg so this preserves e.g. quoted spaces on the command line
		break
		;;
	*)
		cmd+=("$1")
		;;
	esac
	shift
done

if [ "${JMX_PORT+x}" == x ]; then
	export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

if [ "${APP_NAME+x}" == "x" ] && [ "${JAVA_OPTS+x}" == "" ]; then
	export JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/banner_configuration.groovy"
fi

if [ ${#cmd[@]} -eq 0 ]; then
	# No command given, if stdin is a tty run a shell, otherwise exit with an error
	if [ -t 0 ]; then
		exec bash
	else
		echo "ERROR: No command given and stdin is not a terminal"
		exit 1
	fi
else
	# Command was given, run the command
	exec "${cmd[@]}"
fi
