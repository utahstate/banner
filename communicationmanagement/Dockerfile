FROM usuit/banner:base-bcm-9.0.84-jdk8-corretto-cacerts

LABEL version="9.12"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/CommunicationManagement/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat CommunicationManagement /usr/local/tomcat/webapps/CommunicationManagement
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/CommunicationManagement/saml
