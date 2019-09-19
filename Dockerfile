FROM harbor.usu.edu/banner/base-bcm:tomcat8.5.45-jre8-corretto
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL version="9.6"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat CommunicationManagement /usr/local/tomcat/webapps/CommunicationManagement
