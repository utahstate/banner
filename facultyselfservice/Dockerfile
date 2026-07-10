FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts
LABEL version=9.24.1
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/FacultySelfService/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/America/Denver /etc/localtime
RUN echo America/Denver> /etc/timezone
USER tomcat
COPY --chown=tomcat:tomcat FacultySelfService /usr/local/tomcat/webapps/FacultySelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/FacultySelfService/saml
