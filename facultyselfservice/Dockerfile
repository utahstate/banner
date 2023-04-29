FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto
LABEL version="9.20.1"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat FacultySelfService /usr/local/tomcat/webapps/FacultySelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/FacultySelfService/saml
COPY --chown=tomcat:tomcat gradeEntry.json /usr/local/tomcat/webapps/FacultySelfService/gradeEntry.json
