#Dockerfile for StudentAPI
FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine

LABEL api_version=9.12

ENV API_VERSION 9.12 \
    TIMEZONE=America/Denver

COPY StudentApi /usr/local/tomcat/webapps/StudentApi
