FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" EmployeeSelfService

FROM usuit/banner-base:9-jdk21-tomcat10-selfservice AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT="${ENVIRONMENT}" APP_NAME="EmployeeSelfService" APP_SHORT_NAME="employeess"
LABEL edu.usu.banner.environment=${ENVIRONMENT} edu.usu.banner.version=${VERSION} edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.self-service="employee"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
