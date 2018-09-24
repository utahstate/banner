#Dockerfile for StudentAPI
FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine

LABEL api_version=9.13

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY StudentApi /usr/local/tomcat/webapps/StudentApi
