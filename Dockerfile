FROM bandock/banner9-admin:tomcat8.5.37-jre8-alpine

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat eTranscriptAPI /usr/local/tomcat/webapps/eTranscriptAPI
COPY --chown=tomcat:tomcat ojdbc7.jar /usr/local/tomcat/lib/ojdbc6.jar
