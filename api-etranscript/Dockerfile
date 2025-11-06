FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" eTranscriptAPI

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="eTranscriptAPI"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="API" edu.usu.banner.api="etranscript"

COPY --chown=tomcat:tomcat --from=build /opt/build/eTranscriptAPI /usr/local/tomcat/webapps/eTranscriptAPI

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
