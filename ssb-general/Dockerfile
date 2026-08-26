ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

# Download WARs
RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerGeneralSsb

# Patch with cleanaddress if needed
COPY --chmod=755 cleanaddress.sh /usr/local/bin/install-cleanaddress
RUN --mount=type=secret,id=cleanaddress_password,env=PASSWORD install-cleanaddress

FROM usuit/banner-base:9-jdk21-tomcat10-selfservice AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT=${ENVIRONMENT} APP_NAME="BannerGeneralSsb" APP_SHORT_NAME="generalss"
LABEL edu.usu.banner.version=${VERSION} \
		edu.usu.banner.environment=${ENVIRONMENT} \
		edu.usu.banner.appName="${APP_NAME}" \
		edu.usu.banner.self-service="general"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
