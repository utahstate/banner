FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto

LABEL version="4.2"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat javax.jms_1.1.1.jar /usr/local/tomcat/lib/javax.jms_1.1.1.jar
COPY --chown=tomcat:tomcat brim /usr/local/tomcat/webapps/brim
