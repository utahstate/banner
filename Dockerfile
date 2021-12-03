#Dockerfile for StudentAPI
FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.72-jdk8-corretto

LABEL api_version=9.25.0.2

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat StudentApi /usr/local/tomcat/webapps/StudentApi
