FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto

LABEL version="9.23"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/StudentSelfService/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat StudentSelfService /usr/local/tomcat/webapps/StudentSelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/StudentSelfService/saml
