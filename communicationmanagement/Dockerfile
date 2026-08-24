ARG VERSION INSTANCE
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION INSTANCE

RUN --mount=type=ssh download-wars "${INSTANCE}" CommunicationManagement

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION INSTANCE
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.instance=${INSTANCE}
ENV INSTANCE=${INSTANCE}

ENV APP_NAME="CommunicationManagement" APP_TYPE="bcm"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
