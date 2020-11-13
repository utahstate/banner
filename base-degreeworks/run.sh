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

# Maintain backwards compatibility if DW_PASSWORD
# aren't set
if [ -z "${DW_PASSWORD}" ]; then
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

setPropFromEnvPointingToFile dw.password "$DW_PASSWORD"

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
  setPropFromEnv dw.jdbc "$DW_JDBC"
  setPropFromEnv dw.username "$DW_USERNAME"
  setPropFromEnv dw.initialsize "$DW_INITIALSIZE"
  setPropFromEnv dw.maxtotal "$DW_MAXTOTAL"
  setPropFromEnv dw.maxactive "$DW_MAXACTIVE"
  setPropFromEnv dw.minidle "$DW_MINIDLE"
  setPropFromEnv dw.maxidle "$DW_MAXIDLE"
  setPropFromEnv dw.maxwait "$DW_MAXWAIT"
fi

if [ -n "$JMX_PORT" ]; then
  export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

exec catalina.sh run
