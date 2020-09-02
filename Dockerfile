FROM edurepo/banner9-selfservice:tomcat8.5.57-jre8-corretto

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone

USER tomcat
COPY --chown=tomcat:tomcat eTranscriptAPI /usr/local/tomcat/webapps/eTranscriptAPI
#COPY --chown=tomcat:tomcat ojdbc7.jar /usr/local/tomcat/lib/ojdbc6.jar
