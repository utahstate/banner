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

  #Enable Theme for BannerAdmin
  if [ "$prop" = "theme.url" ]; then
    sed -i "2i <param name=\"APP_CSS_URL\" value=\"$val\" />\\n<param name=\"APP_CSS_APPEND\" value=\"true\" />" /usr/local/tomcat/webapps/BannerAccessMgmt/config.xml
  fi

  #Set CAS server for BannerAdmin.ws
  if [ "$prop" = "cas.url" ]; then
    sed -i "s|^cas\.server\.location.*|cas\.server\.location = $val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
  fi

  #Set Banner9.baseurl for BannerAdmin.ws
  if [ "$prop" = "banner9.baseurl" ]; then
    sed -i "s|^webapp\.location.*|webapp\.location = $val\/\${webapp.context}|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
    sed -i "s|^webapp\.wrksp\.location.*|webapp\.wrksp\.location = $val\/\${webapp.wrksp.context}\/|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
    sed -i "s|<param name=\"APPNAV_HELP_URL\".*|<param name=\"APPNAV_HELP_URL\" value=\"$val\/bannerHelp\/Main?page=\" \/>|g" /usr/local/tomcat/webapps/BannerAccessMgmt/config.xml
  fi

  if [ "$prop" = "saml.keystore.env" ]; then
    sed -i "s|^saml\.keystore = .*|saml\.keystore = file://$val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
    cp /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/applicationContext.xml.saml /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/applicationContext.xml
  fi
  
  if [ "$prop" = "saml.keystore.password.env" ]; then
    sed -i "s|^saml\.keystore\.password.*|saml\.keystore\.password = $val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
  fi
  
  if [ "$prop" = "saml.sign.key.alias.env" ]; then
    sed -i "s|^saml\.sign\.key\.alias.*|saml\.sign\.key\.alias = $val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
  fi
  
  if [ "$prop" = "saml.sign.key.password.env" ]; then
    sed -i "s|^saml\.sign\.key\.password.*|saml\.sign\.key\.password = $val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
  fi
  
  if [ "$prop" = "saml.sp.metadata.filename.env" ]; then
    sed -i "s|^saml\.sp\.metadata\.filename.*|saml\.sp\.metadata\.filename = $val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
  fi
  
  if [ "$prop" = "saml.idp.metadata.filename.env" ]; then
    sed -i "s|^saml\.idp\.metadata\.filename.*|saml\.idp\.metadata\.filename = $val|g" /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/config.properties
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

if [ -f "$CONFIG_FILE" ]; then
    setPropsFromFile "$CONFIG_FILE"
fi

setPropFromEnvPointingToFile banproxy.password "$BANPROXY_PASSWORD"

# If BANPROXY_PASSWORD is not set then use secrets to maintain backwards
# compatibility
if [ -z "$BANPROXY_PASSWORD" ]; then
  if [ -d /run/secrets ]; then
    for file in /run/secrets/*; do
      prop=$(basename "$file")
      val=$(cat "$file")
      setProperty "$prop" "$val"
    done
  fi
fi

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
  setPropFromEnv banproxy.maxactive "$BANPROXY_MAXACTIVE"
  setPropFromEnv banproxy.maxtotal "$BANPROXY_MAXTOTAL"
  setPropFromEnv banproxy.maxidle "$BANPROXY_MAXIDLE"
  setPropFromEnv banproxy.maxwait "$BANPROXY_MAXWAIT"
  setPropFromEnv cas.url "$CAS_URL"
  setPropFromEnv banner9.baseurl "$BANNER9_URL"
  setPropFromEnv theme.url "$THEME_URL"
  setPropFromEnv remove.abandoned.on.maintenance "$REMOVE_ABANDONED_ON_MAINTENANCE"
  setPropFromEnv remove.abandoned.on.borrow "$REMOVE_ABANDONED_ON_BORROW"
  setPropFromEnv remove.abandoned.timeout "$REMOVE_ABANDONED_TIMEOUT"
  setPropFromEnv log.abandoned "$LOG_ABANDONED"
  setPropFromEnv bannerdb.rowPrefetch "$DEFAULT_ROW_PREFETCH"
  setPropFromEnv saml.keystore.password.env "$KEYSTORE_PASSWORD"
  setPropFromEnv saml.keystore.env "$KEYSTORE_LOCATION"
  setPropFromEnv saml.sign.key.alias.env "$KEYSTORE_ALIAS"
  setPropFromEnv saml.sign.key.password.env "$KEYSTORE_PASSWORD"
  setPropFromEnv saml.sp.metadata.filename.env "$SP_METADATA"
  setPropFromEnv saml.idp.metadata.filename.env "$IDP_METADATA"
fi

if [ -n "$JMX_PORT" ]; then
  export CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
fi

exec catalina.sh run
