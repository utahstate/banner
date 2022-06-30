#Dockerfile for Banner Integration API
FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.81-jdk8-corretto

LABEL api_version=9.27

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat IntegrationApi /usr/local/tomcat/webapps/IntegrationApi
