#Dockerfile for StudentAPI
FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV API_VERSION 9.11 \
    TIMEZONE=America/Denver

COPY StudentApi /usr/local/tomcat/webapps/StudentApi
