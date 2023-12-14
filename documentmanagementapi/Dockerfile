FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts

ENV TIMEZONE=America\Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/DocumentManagementApi/WEB-INF/classes/banner_configuration.groovy"

USER root
RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender
USER tomcat
COPY --chown=tomcat:tomcat DocumentManagementApi /usr/local/tomcat/webapps/DocumentManagementApi
