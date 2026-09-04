ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root in the container.

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" applicationNavigator

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT=${ENVIRONMENT} APP_SHORT_NAME="appnav" APP_NAME="applicationNavigator" APP_TYPE="self-service"
LABEL edu.usu.banner.version="${VERSION}" edu.usu.banner.environment="${ENVIRONMENT}" edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/${APP_NAME}/META-INF/context.xml

