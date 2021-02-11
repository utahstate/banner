FROM edurepo/banner9-selfservice:tomcat8.5.57-jre8-corretto

LABEL version="2.8.0"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat javax.jms_1.1.1.jar /usr/local/tomcat/lib/javax.jms_1.1.1.jar
COPY --chown=tomcat:tomcat brim /usr/local/tomcat/webapps/brim
