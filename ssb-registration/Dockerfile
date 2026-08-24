FROM usuit/banner-base:build-jdk17-latest AS build

# Download WARs
RUN --mount=type=ssh download-wars "${instance}" StudentRegistrationSsb

FROM usuit/banner-base:9-jdk17-tomcat10 AS final

ENV APP_NAME="StudentRegistrationSsb" APP_TYPE="self-service"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.self-service="registration"

RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ /opt/banner/extensions/ss_ext/i18n/

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
