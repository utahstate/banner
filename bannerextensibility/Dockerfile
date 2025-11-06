FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" BannerExtensibility

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS final

ENV APP_NAME="BannerExtensibility"
LABEL edu.usu.banner.appName="${APP_NAME}"

# /opt/banner exists in banner-base:9 images owned by tomcat, so we can create the directories we need without being root.
RUN ["bash", "-c", "mkdir -p /opt/banner/extensibility/{pb/{i18n,page,css,virtdom},themes"}

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
