#Dockerfile for Banner Integration API
FROM bandock/banner9-selfservice:tomcat8.5.41-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL api_version=9.16

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat IntegrationApi /usr/local/tomcat/webapps/IntegrationApi
