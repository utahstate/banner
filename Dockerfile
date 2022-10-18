FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.81-jdk8-corretto
LABEL version="9.19"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat FacultySelfService /usr/local/tomcat/webapps/FacultySelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/FacultySelfService/saml
