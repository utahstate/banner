#Dockerfile for Banner Integration API
FROM harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat8-java8
MAINTAINER "Eric Allen <eric.allen@usu.edu>

ENV INTERGRATION_API 9.10

COPY IntegrationApi.war /usr/local/tomcat/webapps/IntegrationApi.war
