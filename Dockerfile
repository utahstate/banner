#Dockerfile for StudentAPI
FROM harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat8-java8
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV API_VERSION 9.8

COPY StudentApi.war /usr/local/tomcat/webapps/StudentApi.war
