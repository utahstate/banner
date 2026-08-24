ARG VERSION INSTANCE
FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION INSTANCE

RUN --mount=type=ssh <<EOS bash
		set -e
# Download and extract files from build.banner
		download-wars "${INSTANCE}" BannerAdmin BannerAdmin.ws

# set sameSiteCookies to none
		sed -i -e 's|\<\!-- Insert Environment.*|<CookieProcessor sameSiteCookies="none" />|g' BannerAdmin.ws/META-INF/context.xml
EOS

# Configure cleanaddress
COPY --chmod=755 cleanaddress.sh /usr/local/bin/install-cleanaddress
RUN install-cleanaddress

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION INSTANCE
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.instance=${INSTANCE}
ENV INSTANCE=${INSTANCE}

ENV APP_NAME="BannerAdmin" APP_TYPE="admin"
LABEL edu.usu.banner.appName="${APP_NAME}" edu.usu.banner.appType="${APP_TYPE}"

COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}/ /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --from=build /opt/build/${APP_NAME}.ws/ /usr/local/tomcat/webapps/${APP_NAME}.ws
COPY --chown=tomcat:tomcat context.xml server.xml web.xml /usr/local/tomcat/conf/
