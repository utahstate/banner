FROM usuit/banner-base:build-jdk17-latest AS build

ARG cleanaddress=false

RUN --mount=type=ssh <<EOS bash
		set -e
# Download and extract files from build.banner
		download-wars "${instance}" BannerAdmin BannerAdmin.ws

# set sameSiteCookies to none
		sed -i -e 's|\<\!-- Insert Environment.*|<CookieProcessor sameSiteCookies="none" />|g' BannerAdmin.ws/META-INF/context.xml

if [ "${cleanaddress}" -eq "true" ]; then
		echo "Updating BannerAdmin.ws config for CleanAddress"
		config_propfile_path="BannerAdmin.ws/WEB-INF/classes"
if ! tr -d [[:space:]] < $config_profile_path/config.properties | grep -iq "plugins=com.runnertech.ext"; then
		cd $config_propfile_path
		cat <<EOF config.properties > config.properties.new
##############################
# Plug-in Definition
##############################

plugins = com.runnertech.ext

EOF
		mv config.properties{.new,}
		cd ../../../
else
		echo "Plugin definition already present."
fi
fi
EOS

FROM usuit/banner-base:9-jdk17-tomcat10-latest AS final

ENV APP_NAME="BannerAdmin"
LABEL edu.usu.banner.appName="${APP_NAME}"

ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XXMaxMetaspaceSize=1024m -Dlog4j.configuration=config.properties -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}/ /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}.ws/ /usr/local/tomcat/webapps/${APP_NAME}.ws
COPY --chown=tomcat:tomcat context.xml server.xml web.xml /usr/local/tomcat/conf/
