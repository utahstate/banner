FROM bandock/banner9-selfservice:tomcat8.5.41-jre8-alpine

LABEL version="9.14"
ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
COPY --chown=tomcat:tomcat server.xml /usr/local/tomcat/conf/server.xml
USER tomcat

RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ \
    && mkdir -p /opt/banner/extensions/ss_ext/i18n/ \
    && chown -R tomcat:tomcat /opt/banner/extensions

RUN sed -i 's/shared.loader=.*/shared.loader=xom-*.jar,bcprov*.jar/' /usr/local/tomcat/conf/catalina.properties

COPY --chown=tomcat:tomcat  StudentRegistrationSsb /usr/local/tomcat/webapps/StudentRegistrationSsb
