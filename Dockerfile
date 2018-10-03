FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine

LABEL version="9.11"
ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ \
    && mkdir -p /opt/banner/extensions/ss_ext/i18n/ \
    && chown -R tomcat:tomcat /opt/banner/extensions

COPY --chown=tomcat:tomcat  StudentRegistrationSsb /usr/local/tomcat/webapps/StudentRegistrationSsb
