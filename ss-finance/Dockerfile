FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" FinanceSelfService

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="FinanceSelfService"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="SelfService" edu.usu.banner.self-service="finance"

COPY --chown=tomcat:tomcat --from=build /opt/build/FinanceSelfService /usr/local/tomcat/webapps/FinanceSelfService

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/FinanceSelfService/WEB-INF/classes/
