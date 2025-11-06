FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" StudentApi

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="StudentApi"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="API" edu.usu.banner.api="student"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
