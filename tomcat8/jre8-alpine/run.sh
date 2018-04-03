#!/bin/sh
# shellcheck disable=SC2013,SC2046
# Thanks to Virginia Tech and College of William and Mary for some of the setup in this file

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
    sed -i "98i <param name=\"APP_CSS_URL\" value=\"$val\" />\\n<param name=\"APP_CSS_APPEND\" value=\"true\" />" /usr/local/tomcat/webapps/BannerAdmin/config.xml
  fi

  #Set CAS server for BannerAdmin.ws
  if [ "$prop" = "cas.url" ]; then
    sed "s/^cas\.server\.location.*/cas\.server\.location = $val/g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
  fi 

  #Set Banner9.baseurl for BannerAdmin.ws
  if [ "$prop" = "banner9.baseurl" ]; then
    sed "s/^webapp\.location.*/webapp\.location = $val\/\${webapp.context}/g" /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/classes/config.properties
    sed "s/<param name=\"APPNAV_HELP_URL\".*/<param name=\"APPNAV_HELP_URL\" value=\"${banner9.baseurl}\/bannerHelp\/Main?page=\" \/>/g" /usr/local/tomcat/webapps/BannerAdmin/config.xml
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

# Pull properties from docker secrets
if [ -d /run/secrets ]; then
  for file in /run/secrets/*; do
    prop=$(basename "$file")
    val=$(cat "$file")
    setProperty "$prop" "$val"
  done
fi

setPropFromEnv() {
  prop=$1
  val=$2
  # If no value was given, abort
  [ -z "$val" ] && return
  if [ $(grep -c $prop $PROPFILE) -eq 0 ]; then
    setProperty $prop $val
  fi
}

if [ -z $CONFIG_FILE ]; then
  setPropFromEnv bannerdb.jdbc "$BANNERDB_JDBC"
  setPropFromEnv banproxy.username "$BANPROXY_USERNAME"
  setPropFromEnv banproxy.password "$BANPROXY_PASSWORD"
  setPropFromEnv banproxy.initialsize "$BANPROXY_INITALSIZE"
  setPropFromEnv banproxy.maxtotal "$BANPROXY_MAXTOTAL"
  setPropFromEnv banproxy.maxidle "$BANPROXY_MAXIDLE"
  setPropFromEnv banproxy.maxwait "$BANPROXY_MAXWAIT"
  setPropFromEnv cas.url "$CAS_URL"
  setPropFromEnv banner9.baseurl "$BANNER9_URL"
  setPropFromEnv theme.url "$THEME_URL"
fi
exec catalina.sh run
