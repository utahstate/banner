#Dockerfile for StudentAPI
FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine

LABEL api_version=9.15

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

RUN sed -i 's/shared.loader=.*/shared.loader=xom-*.jar, bcprov*.jar/' /usr/local/tomcat/conf/catalina.properties
COPY StudentApi /usr/local/tomcat/webapps/StudentApi
