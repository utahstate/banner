FROM usuit/banner-base:build-jdk17-latest AS build
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root.

RUN --mount=type=ssh <<EOF bash
		set -e
# Download files from build.banner and extract
		download-wars "${instance}" BannerAccessMgmt{,.ws}

EOF

FROM usuit/banner-base:9-jdk17-tomcat10 AS final

ENV APP_NAME="BannerAccessMgmt" APP_TYPE="admin"
LABEL	edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}"

ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XXMaxMetaspaceSize=1024m -Dlog4j.configuration=config.properties -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}.ws /usr/local/tomcat/webapps/${APP_NAME}.ws

# Load in config files
COPY --chown=tomcat:tomcat context.xml server.xml web.xml /usr/local/tomcat/conf/
