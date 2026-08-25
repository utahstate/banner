ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

# Download WARs
RUN --mount=type=ssh download-wars "${ENVIRONMENT}" StudentRegistrationSsb

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION ENVIRONMENT
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT}
ENV ENVIRONMENT=${ENVIRONMENT}

ENV APP_NAME="StudentRegistrationSsb" APP_TYPE="self-service"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.self-service="registration"

RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ /opt/banner/extensions/ss_ext/i18n/

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
