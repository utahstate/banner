FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" StudentApi

FROM usuit/banner-base:9-jdk21-tomcat10-api AS final
ARG VERSION ENVIRONMENT

ENV APP_NAME="StudentApi" APP_TYPE="api"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.api="student"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
