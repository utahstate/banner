ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root.

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerAccessMgmt BannerAccessMgmt.ws
RUN configure-saml BannerAccessMgmt

FROM usuit/banner-base:9-jdk21-tomcat10-admin AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT="${ENVIRONMENT}" APP_NAME="BannerAccessMgmt" APP_SHORT_NAME="bam"
LABEL	edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT} edu.usu.banner.appName="${APP_NAME}"

ENV MAXMETASPACE=1024m

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}.ws /usr/local/tomcat/webapps/${APP_NAME}.ws

COPY --chown=tomcat:tomcat --chmod=755 BannerAccessMgmt_run.sh /usr/local/tomcat/bin/run_BannerAccessMgmt.sh
