#!/bin/sh
# shellcheck disable=SC2013,SC2046
# Thanks to Virginia Tech and College of William and Mary for some of the setup in this file

# Fail fast
set -e

PROPFILE="/usr/local/tomcat/conf/catalina.properties"
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
    sed -i "s/$prop=.*/$prop=$val/" "$PROPFILE"
  fi
}

setPropsFromFile() {
  file=$1
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

setPropFromEnvPointingToFile banproxy.password "$BANPROXY_PASSWORD"
setPropFromEnvPointingToFile banssuser.password "$BANSSUSER_PASSWORD"

setPropFromEnv() {
  prop=$1
  val=$2
  # If no value was given, abort
  [ -z "$val" ] && return
  if [ $(grep -c "$prop" $PROPFILE) -eq 0 ]; then
    setProperty "$prop" "$val"
  fi
}

if [ -z "$CONFIG_FILE" ]; then
  setPropFromEnv bannerdb.jdbc "$BANNERDB_JDBC"
  setPropFromEnv banproxy.username "$BANPROXY_USERNAME"
  setPropFromEnv banproxy.initialsize "$BANPROXY_INITALSIZE"
  setPropFromEnv banproxy.maxtotal "$BANPROXY_MAXTOTAL"
  setPropFromEnv banproxy.maxactive "$BANPROXY_MAXACTIVE"
  setPropFromEnv banproxy.maxidle "$BANPROXY_MAXIDLE"
  setPropFromEnv banproxy.maxwait "$BANPROXY_MAXWAIT"
  setPropFromEnv banssuser.username "$BANSSUSER_USERNAME"
  setPropFromEnv banssuser.initialsize "$BANSSUSER_INITALSIZE"
  setPropFromEnv banssuser.maxtotal "$BANSSUSER_MAXTOTAL"
  setPropFromEnv banssuser.maxactive "$BANSSUSER_MAXACTIVE"
  setPropFromEnv banssuser.maxidle "$BANSSUSER_MAXIDLE"
  setPropFromEnv banssuser.maxwait "$BANSSUSER_MAXWAIT"
  setPropFromEnv cas.url "$CAS_URL"
  setPropFromEnv banner9.baseurl "$BANNER9_URL"
  setPropFromEnv remove.abandoned.on.maintenance "$REMOVE_ABANDONED_ON_MAINTENANCE"
  setPropFromEnv remove.abandoned.on.borrow "$REMOVE_ABANDONED_ON_BORROW"
  setPropFromEnv remove.abandoned.timeout "$REMOVE_ABANDONED_TIMEOUT"
  setPropFromEnv log.abandoned "$LOG_ABANDONED"
  setPropFromEnv bannerdb.rowPrefetch "$DEFAULT_ROW_PREFETCH"
fi

if [ -n "$JMX_PORT" ]; then
  export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

exec catalina.sh run
