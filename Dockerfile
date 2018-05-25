#Dockerfile for StudentAPI
FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine

LABEL api_version=9.11.0.1

ENV API_VERSION 9.11.0.1 \
    TIMEZONE=America/Denver

COPY StudentApi /usr/local/tomcat/webapps/StudentApi
