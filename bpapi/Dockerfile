FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" BannerAdminBPAPI

FROM usuit/banner-base:9-jdk17-tomcat10-latest AS final

ENV APP_NAME="BannerAdminBPAPI"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="API" edu.usu.banner.api="BusinessProcess"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}
COPY applicationContext.xml /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/
