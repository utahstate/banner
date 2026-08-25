FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION INSTANCE

RUN --mount=type=ssh download-wars "${INSTANCE}" StudentSelfService

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION INSTANCE

ENV APP_NAME="StudentSelfService" APP_TYPE="self-service"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.self-service="student"

COPY --chown=tomcat:tomcat server.xml /usr/local/tomcat/conf/server.xml
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
