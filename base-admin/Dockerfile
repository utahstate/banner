FROM usuit/banner-base:9-jdk21-tomcat10

ENV APP_TYPE="admin"
LABEL edu.usu.banner.appType="${APP_TYPE}"

COPY --chown=tomcat:tomcat server.xml context.xml web.xml /usr/local/tomcat/conf/
