FROM usuit/banner-base:build-jdk17-latest AS build

RUN --mount=type=ssh download-wars "${instance}" EmployeeSelfService

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS build

ENV APP_NAME="EmployeeSelfService"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="SelfService" edu.usu.banner.self-service="employee"

COPY --chown tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown tomcat:tomcat context.xml /usr/local/tomcat/webapps/${APP_NAME}/META-INF/context.xml

# Apply patches
COPY --chown tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
