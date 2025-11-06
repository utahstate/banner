FROM usuit/banner-base:build-jdk17-latest AS build
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root in the container.

RUN --mount=type=ssh download-wars "${instance}" applicationNavigator

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS final

ENV APP_NAME="applicationNavigator"
LABEL edu.usu.banner.appName="${APP_NAME}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/${APP_NAME}/META-INF/context.xml

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
