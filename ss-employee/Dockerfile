FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" EmployeeSelfService

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="EmployeeSelfService"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="SelfService" edu.usu.banner.self-service="employee"

COPY --chown tomcat:tomcat --from=build /opt/build/EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
COPY --chown tomcat:tomcat context.xml /usr/local/tomcat/webapps/EmployeeSelfService/META-INF/context.xml

# Apply patches
COPY --chown tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/EmployeeSelfService/WEB-INF/classes/
