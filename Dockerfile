#Dockerfile for StudentAPI
FROM edurepo/banner9-selfservice:tomcat8.5.56-jre8-corretto

LABEL api_version=9.20

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat


COPY --chown=tomcat:tomcat StudentApi /usr/local/tomcat/webapps/StudentApi
