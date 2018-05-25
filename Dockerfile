#Dockerfile for Banner Integration API
FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL api_version=9.11.0.1

ENV API_VERSION 9.11.0.1 \
    TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat IntegrationApi /usr/local/tomcat/webapps/IntegrationApi
