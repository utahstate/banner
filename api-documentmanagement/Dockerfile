FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" DocumentManagementApi

FROM usuit/banner-base:9-jdk17-tomcat10 AS final

ENV APP_NAME="DocumentManagementApi" APP_TYPE="api"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.api="DocumentManagement"

RUN mkdir -p /opt/banner/xtender
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
