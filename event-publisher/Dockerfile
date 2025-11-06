FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" BannerEventPublisher

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS final

ENV APP_NAME="BannerEventPublisher"
LABEL edu.usu.banner.appName="${APP_NAME}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/

