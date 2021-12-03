FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.72-jdk8-corretto

ENV TIMEZONE=America\Denver

USER root
RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender
USER tomcat
COPY --chown=tomcat:tomcat DocumentManagementApi /usr/local/tomcat/webapps/DocumentManagementApi