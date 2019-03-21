FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL version="9.4"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat CommunicationManagement /usr/local/tomcat/webapps/CommunicationManagement
