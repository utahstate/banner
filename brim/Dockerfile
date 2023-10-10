FROM usuit/banner:base-bannerselfservice-9.0.80-jdk8-corretto

LABEL version="4.4"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/brim/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat javax.jms_1.1.1.jar /usr/local/tomcat/lib/javax.jms_1.1.1.jar
COPY --chown=tomcat:tomcat brim /usr/local/tomcat/webapps/brim
