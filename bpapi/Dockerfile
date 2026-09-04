ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerAdminBPAPI

RUN --mount=type=ssh <<EOS bash
		set -e
		scp -r "build.banner.usu.edu:/${ENVIRONMENT}/BannerAdminBPAPI" ./configs
		cd configs
		sed -i -e "s|u01/deploy/${ENVIRONMENT}|usr/local/tomcat/webapps|g" config/config.xml
		sed -i -e "s|u01/deploy/${ENVIRONMENT}|usr/local/tomcat/webapps|g" config/config.properties
EOS

FROM usuit/banner-base:9-jdk21-tomcat10-admin AS final
ARG VERSION ENVIRONMENT
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT} edu.usu.banner.appType=admin-api
ENV ENVIRONMENT=${ENVIRONMENT} APP_TYPE=admin-api

ENV APP_NAME="BannerAdminBPAPI"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.api="BusinessProcess"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/configs /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/configs/config /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/config
COPY --chown=tomcat:tomcat --from=build /opt/build/configs/config/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/

COPY applicationContext.xml /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/
