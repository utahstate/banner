ARG VERSION ENVIRONMENT
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh <<EOS bash
    set -e
# Download and extract files from build.banner
    download-wars "${ENVIRONMENT}" BannerAdmin BannerAdmin.ws

# set sameSiteCookies to none
    sed -i -e 's|\<\!-- Insert Environment.*|<CookieProcessor sameSiteCookies="none" />|g' BannerAdmin.ws/META-INF/context.xml

# run SAML metaconfiguration script
		configure-saml BannerAdmin
EOS

# Configure cleanaddress
COPY --chmod=755 cleanaddress.sh /usr/local/bin/install-cleanaddress
RUN install-cleanaddress

FROM usuit/banner-base:9-jdk21-tomcat10-admin AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT=${ENVIRONMENT} APP_NAME="BannerAdmin"
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.environment=${ENVIRONMENT} edu.usu.banner.appName="${APP_NAME}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}/ /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}.ws/ /usr/local/tomcat/webapps/${APP_NAME}.ws
COPY --chown=tomcat:tomcat applicationContext.xml.saml /usr/local/tomcat/webapps/${APP_NAME}.ws/WEB-INF/applicationContext.xml

# Add the runtime SAML config script
COPY --chown=tomcat:tomcat --chmod=755 BannerAdmin_run.sh /usr/local/tomcat/bin/run_BannerAdmin.sh
