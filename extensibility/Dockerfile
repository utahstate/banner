ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerExtensibility

FROM usuit/banner-base:9-jdk21-tomcat10-selfservice AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT=${ENVIRONMENT} APP_NAME="BannerExtensibility" APP_SHORT_NAME="extensibility"
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT} edu.usu.banner.appName="${APP_NAME}"

# /opt/banner exists in banner-base:9 images owned by tomcat, so we can create the directories we need without being root.
RUN bash -c "mkdir -p /opt/banner/extensibility/{pb/{i18n,page,css,virtdom},themes}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
