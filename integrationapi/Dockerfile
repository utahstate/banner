#Dockerfile for Banner Integration API
FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto

LABEL api_version=9.29

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat IntegrationApi /usr/local/tomcat/webapps/IntegrationApi
