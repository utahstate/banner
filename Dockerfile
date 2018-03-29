FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV TIMEZONE America/Denver

COPY applicationNavigator /usr/local/tomcat/webapps/applicationNavigator

USER root
RUN chown -R tomcat:tomcat /usr/local/tomcat/webapps/applicationNavigator
USER tomcat