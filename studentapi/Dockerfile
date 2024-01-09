#Dockerfile for StudentAPI
FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts

LABEL api_version=9.32

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/StudentApi/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat StudentApi /usr/local/tomcat/webapps/StudentApi
