FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts
LABEL version=9.26.2
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/StudentSelfService/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/America/Denver /etc/localtime
RUN echo America/Denver> /etc/timezone
USER tomcat
COPY --chown=tomcat:tomcat StudentSelfService /usr/local/tomcat/webapps/StudentSelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/StudentSelfService/saml
