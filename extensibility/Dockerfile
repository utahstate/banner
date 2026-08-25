ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerExtensibility

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION ENVIRONMENT
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT}
ENV ENVIRONMENT=${ENVIRONMENT}

ENV APP_NAME="BannerExtensibility" APP_TYPE="self-service"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}"

# /opt/banner exists in banner-base:9 images owned by tomcat, so we can create the directories we need without being root.
RUN bash -c "mkdir -p /opt/banner/extensibility/{pb/{i18n,page,css,virtdom},themes}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
