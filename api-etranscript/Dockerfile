FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" eTranscriptAPI

FROM usuit/banner-base:9-jdk21-tomcat10-api AS final
ARG VERSION ENVIRONMENT

ENV APP_NAME="eTranscriptAPI" APP_TYPE="api"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.api="etranscript"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
