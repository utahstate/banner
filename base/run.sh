#!/bin/bash
# Thanks to Virginia Tech and College of William and Mary for some of the setup in this file


# Fail fast
set -e

PROPFILE="/usr/local/tomcat/conf/catalina.properties"
PROPDIR=${PROPERTY_DROPINS:"/usr/local/tomcat/conf/catalina.properties.d"}
if [ ! -f "$PROPFILE" ]; then
  echo "Unable to find properties file $PROPFILE"
  exit 1
fi

setProperty() {
  prop=$1
  val=$2

  if [ -f "/usr/local/tomcat/webapps/*/WEB-INF/web.xml" ]; then
    if [ "$prop" = "banner9.baseurl" ]; then
      for settings_file in /usr/local/tomcat/webapps/*/WEB-INF/web.xml; do
        xmlstarlet ed  --inplace -N x="http://java.sun.com/xml/ns/javaee" -u "/x:web-app/x:filter[x:filter-name[normalize-space(text())='CAS Validation Filter']]/x:init-param[x:param-name[normalize-space(text())='serverName']]/x:param-value" -v "$val" "$settings_file"
      done
    fi

    if [ "$prop" = "cas.url" ]; then
      for settings_file in /usr/local/tomcat/webapps/*/WEB-INF/web.xml; do
        xmlstarlet ed  --inplace -N x="http://java.sun.com/xml/ns/javaee" -u "/x:web-app/x:filter[x:filter-name[normalize-space(text())='CAS Validation Filter']]/x:init-param[x:param-name[normalize-space(text())='casServerUrlPrefix']]/x:param-value" -v "$val" "$settings_file"
      done
    fi
  fi

  if [ $(grep -c "$prop" "$PROPFILE") -eq 0 ]; then
    echo "${prop}=$val" >> "$PROPFILE"
  else
    val=$(echo "$val" |sed 's#/#\\/#g')
    sed -i "s/$prop=.*/### Overridden at startup\n$prop=$val/" "$PROPFILE"
  fi
}

setPropsFromFile() {
  file=$1
	echo "### Section transcluded from ${file}" >> "$PROPFILE"
  for l in $(grep '=' "$file" | grep -v '^ *#'); do
    prop=$(echo "$l" |cut -d= -f1)
    val=$(echo "$l" |cut -d= -f2)
    setProperty "$prop" "$val"
  done
}

if [ -f "$CONFIG_FILE" ]; then
    setPropsFromFile "$CONFIG_FILE"
fi

# Maintain backwards compatibility if BANPROXY_PASSWORD and BANSSUSER_PASSWORD
# aren't set
if [ -z "${BANPROXY_PASSWORD}" ] && [ -z "${BANSSUSER_PASSWORD}" ]; then
  if [ -d /run/secrets ]; then
    for file in /run/secrets/*; do
      prop=$(basename "$file")
      val=$(cat "$file")
      setProperty "$prop" "$val"
    done
  fi
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
setPropFromEnvPointingToFile banproxy.password "$BANPROXY_PASSWORD"
setPropFromEnvPointingToFile banssuser.password "$BANSSUSER_PASSWORD"
setPropFromEnvPointingToFile commmgr.password "$COMMMGR_PASSWORD"

# Unset the password vars so they aren't easily readable by other processes
unset BANPROXY_PASSWORD
unset BANSSUSER_PASSWORD
unset COMMMGR_PASSWORD

setPropFromEnv() {
  prop=$1
  val=$2
  # If no value was given, abort
  [ -z "$val" ] && return
  if [ $(grep -c "$prop" $PROPFILE) -eq 0 ]; then
    setProperty "$prop" "$val"
  fi
}

# Merge in all property files (files in $PROPDIR that end in .properties)
for propFile in ${PROPDIR}/*.properties; do
	if [[ "${propFile}" == "${PROPDIR}/*.properties" ]]; then
		echo "WARN: No dropin config files found, did you forget to mount the config volume?" >&2
		break;
	fi
	setPropsFromFile $propFile
done

echo "### BEGIN SECTION: Imported from BANNER_ environment" >> "${PROPFILE}"

# Dynamically scan the environment for BANNER_* environment variables and set their associated properties.
# Any environment variable of the form BANNER_PATH_TO_PROP will be used to set a property of the form "path.to.prop".
for var in $(env | gawk 'match($0, /^(BANNER_[A-Z0-9_]+)=.*$/, ary) { print ary[1] }'); do
	# remove banner prefix, lowercase the string, and replace underscores with dots
	propname=${var/BANNER_/}
	propname=${propname,,}
	propname=${propname//_/.}
	# set the property (we don't use setPropFromEnv because it is designed to not override existing properties,
	# which we explicitly would like to have happen -- most configuration should be done in the config files.
	setProperty "${propname}" "${!var}"
done

echo "### BEGIN SECTION: Imported from command line arguments" >> "${PROPFILE}"

# Finally, set properties that are configured on the command line
while [ $1 != '' ]; do
	if [ $1 == '--' ]; then
		# end of proplist, might be followed by a command.
		shift
		break;
	fi
	propName=$(echo $1 | cut -d= -f1)
	propVal=$(echo $1 | cut -d= -f2-)
	setProperty "${propName}" "${propVal}"
	shift
done


if [ -n "$JMX_PORT" ]; then
  export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

if ! [ -v "${APP_NAME+x}" ] && [ -v "${JAVA_OPTS+x}" ]; then
	export JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/banner_configuration.groovy"
fi

# We do this so we can easily debug after all property and environment setup has been done.
if [ $# -eq 0 ]; then
	# If we parsed all arguments above (no '--' in arg list), run as normal
	exec catalina.sh run
else
	# Otherwise, we're running in development, the remaining arg list is a command that we should run instead of starting the server.
	exec "$@"
fi
