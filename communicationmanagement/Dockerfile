FROM usuit/banner:base-bcm-9.0.71-jdk8-corretto

LABEL version="9.8"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat CommunicationManagement /usr/local/tomcat/webapps/CommunicationManagement
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/CommunicationManagement/saml
