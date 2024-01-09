FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts

LABEL version="3.13"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/applicationNavigator/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat applicationNavigator /usr/local/tomcat/webapps/applicationNavigator
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/applicationNavigator/META-INF/context.xml
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/applicationNavigator/saml
