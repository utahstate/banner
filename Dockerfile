FROM edurepo/banner9-selfservice:tomcat8.5.57-jre8-corretto

ENV TIMEZONE=America\Denver

USER root
RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender
USER tomcat
COPY --chown=tomcat:tomcat DocumentManagementApi /usr/local/tomcat/webapps/DocumentManagementApi