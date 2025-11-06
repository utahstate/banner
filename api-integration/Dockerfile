FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" IntegrationApi

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="IntegrationApi"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="API" edu.usu.banner.api="integration"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/

