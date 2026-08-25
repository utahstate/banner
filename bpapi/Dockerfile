ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" BannerAdminBPAPI

RUN --mount=type=ssh <<EOS bash
		set -e
		scp "build.banner.usu.edu:/u01/deploy/${ENVIRONMENT}/BannerAdminBPAPI/BannerAdminBPAPI_configs.zip" .
		mkdir configs
		cd configs
		unzip ../BannerAdminBPAPI_configs.zip
		sed -i -e "s|u01/deploy/${ENVIRONMENT}|usr/local/tomcat/webapps|g" config/config.xml
		sed -i -e "s|u01/deploy/${ENVIRONMENT}|usr/local/tomcat/webapps|g" config/config.properties
EOS

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION ENVIRONMENT
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT}
ENV ENVIRONMENT=${ENVIRONMENT}

ENV APP_NAME="BannerAdminBPAPI" APP_TYPE="admin-api"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}" edu.usu.banner.api="BusinessProcess"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/configs /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/configs/config /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/config
COPY --chown=tomcat:tomcat --from=build /opt/build/configs/config/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/

COPY applicationContext.xml /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/
