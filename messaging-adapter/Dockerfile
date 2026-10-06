FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION
ENV APP_NAME="EllucianMessagingAdapter" APP_SHORT_NAME="ema" APP_TYPE="adapter"
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.appName=${APP_NAME} edu.usu.banner.appType=${APP_TYPE}

COPY --chown=tomcat:tomcat ${APP_NAME} /usr/local/tomcat/webapps/${APP_NAME}
COPY --chown=tomcat:tomcat --chmod=755 health-check.sh /usr/local/tomcat/bin/health-check.sh
COPY --chown=tomcat:tomcat --chmod=755 run_EllucianMessagingAdapter.sh /usr/local/tomcat/bin/
