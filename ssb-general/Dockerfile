ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

# Download WARs
RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerGeneralSsb

# Patch with cleanaddress if needed
COPY --chmod=755 cleanaddress.sh /usr/local/bin/install-cleanaddress
RUN --mount=type=secret,id=cleanaddress_password,env=PASSWORD install-cleanaddress

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION ENVIRONMENT
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT}
ENV ENVIRONMENT=${ENVIRONMENT}

ENV APP_NAME="BannerGeneralSsb" APP_TYPE="self-service"
LABEL edu.usu.banner.appName="${APP_NAME}" \
		edu.usu.banner.appType="${APP_TYPE}" \
		edu.usu.banner.self-service="general"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/

