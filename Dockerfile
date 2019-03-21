FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL version="9.5"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

RUN sed -i 's/shared.loader=.*/shared.loader=xom-*.jar,bcprov*.jar/' /usr/local/tomcat/conf/catalina.properties

COPY --chown=tomcat:tomcat CommunicationManagement /usr/local/tomcat/webapps/CommunicationManagement
