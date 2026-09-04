#!/bin/bash
# Thanks to Virginia Tech and College of William and Mary for some of the setup in this file

# Fail fast
set -euo pipefail

function on-error {
	if [ -t 0 ]; then
		echo -e "\033[1;31mFatal error ocurred during app startup. Failing command: '$BASH_COMMAND'. Dropping to debug shell.\033[0m"
		exec bash
	else
		echo "Fatal error occurred during app startup. Failing command: '$BASH_COMMAND'. Exiting."
	fi
}
trap on-error ERR

if [[ "${DEBUG_STARTUP_SCRIPT_AND_LOG_PASSWORDS+x}" == 'x' ]]; then
	echo "INFO: enabling debug mode. WARNING: this will log any secrets handled by the script to the console!"
	set -x
fi

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
		sed -i "\|<param name=\"APP_CSS_URL\.*|d" /usr/local/tomcat/webapps/"$APP_NAME"/config.xml
		sed -i "\|<param name=\"APP_CSS_APPEND\.*|d" /usr/local/tomcat/webapps/"$APP_NAME"/config.xml
		sed -i "2i <param name=\"APP_CSS_URL\" value=\"$val\" />\\n<param name=\"APP_CSS_APPEND\" value=\"true\" />" /usr/local/tomcat/webapps/"$APP_NAME"/config.xml
	fi

	#Set Banner9.baseurl for BannerAdmin.ws and appnav links in BannerAdmin
	if [ "$prop" = "banner9.baseurl" ]; then
		sed -i "s|^webapp\.location.*|webapp\.location = $val\/\${webapp.context}|g" /usr/local/tomcat/webapps/"$APP_NAME".ws/WEB-INF/classes/config.properties
		sed -i "s|^webapp\.wrksp\.location.*|webapp\.wrksp\.location = $val\/\${webapp.wrksp.context}|g" /usr/local/tomcat/webapps/"$APP_NAME".ws/WEB-INF/classes/config.properties
		#sed -i "s|<param name=\"APPNAV_HELP_URL\".*|<param name=\"APPNAV_HELP_URL\"   value=\"$val/bannerHelp/Main?page=\" />|g" /usr/local/tomcat/webapps/BannerAdmin/config.xml
		sed -i "s|<param name=\"APPNAV_API_URL\".*|<param name=\"APPNAV_API_URL\" value=\"$val/applicationNavigator/static/dist/m.js\" />|g" /usr/local/tomcat/webapps/"$APP_NAME"/config.xml
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
	admin)
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
	for file in /run/passwords/db-*; do
		if [[ -f "${file}/username" ]] && [[ -f "${file}/password" ]]; then
			# extract the property namespace for this database user
			propns="${file##*/db-}"
			echo "INFO: configuring database connection for ${propns}"
			read username <"$file/username" || :
			read password <"$file/password" || :
			echo "### Configuration for database connection ${propns}" >>"${PROPFILE}"
			setProperty "${propns}.username" "$username"
			setProperty "${propns}.password" "$password"
			if [[ -f "/run/app-config/database.properties.d/${propns}.properties" ]]; then
				setPropsFromFile <(while read line; do echo "${propns}.${line}"; done <"/run/app-config/database.properties.d/${propns}.properties")
			else
				setPropsFromFile <(while read line; do echo "${propns}.${line}"; done <"/etc/default-database-connection.properties")
			fi
		else
			echo "WARN: username and/or password files not found for ${file##*/db-}!"
			ls "${file}"
		fi
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

setPropFromEnv() {
	prop=$1
	val=$2
	# If no value was given, abort
	[ -z "$val" ] && return
	if [ $(grep -c "$prop" $PROPFILE) -eq 0 ]; then
		setProperty "$prop" "$val"
	fi
}

# If an app-specific run script is present and executable, source it. We source instead of executing so that it has access to the property manglement API
[ -x "/usr/local/tomcat/bin/run_${APP_NAME}.sh" ] && source "/usr/local/tomcat/bin/run_${APP_NAME}.sh"

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

# Copy application groovy files into place since these apps can't read symlinks FOR SOME REASON
find webapps -type l -name '*.groovy' -printf %p: -execdir readlink {} \; | while IFS=: read dest source; do
	echo "INFO: Copying Groovy file $(basename $dest) into place"
	rm -- "$dest"
	if ! cp -- "$source" "$dest"; then
		if [[ -f "${dest}".shipped ]]; then
			echo "INFO: Customized Groovy script not found for $(basename -- "$dest"), restoring shipped file"
		else
			echo "ERROR: Customized Groovy script not found for  $(basename -- "$dest") but no shipped file available."
			echo "App is not configured, cannot operate."
			exit 1
		fi
	fi
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
