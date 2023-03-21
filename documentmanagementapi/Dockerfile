FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto

ENV TIMEZONE=America\Denver

USER root
RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender
USER tomcat
COPY --chown=tomcat:tomcat DocumentManagementApi /usr/local/tomcat/webapps/DocumentManagementApi
