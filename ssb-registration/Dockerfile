ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

# Download WARs
RUN --mount=type=ssh download-wars "${ENVIRONMENT}" StudentRegistrationSsb

FROM usuit/banner-base:9-jdk21-tomcat10-selfservice AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT=${ENVIRONMENT} APP_NAME="StudentRegistrationSsb" APP_SHORT_NAME="registrationss"
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT} edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.self-service="registration"

RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ /opt/banner/extensions/ss_ext/i18n/

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
