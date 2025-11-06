FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" FacultySelfService

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="FacultySelfService"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="SelfService" edu.usu.banner.self-service="faculty"

COPY --chown=tomcat:tomcat --from=build /opt/build/FacultySelfService /usr/local/tomcat/webapps/FacultySelfService

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/FacultySelfService/WEB-INF/classes/
