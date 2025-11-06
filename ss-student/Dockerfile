FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" StudentSelfService

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="StudentSelfService"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="SelfService" edu.usu.banner.self-service="student"

COPY --chown tomcat:tomcat --from=build /opt/build/StudentSelfService /usr/local/tomcat/webapps/StudentSelfService

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/StudentSelfService/WEB-INF/classes/
