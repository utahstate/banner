FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false \
    TIMEZONE=America\Denver

RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ \
    && mkdir -p /opt/banner/extensions/ss_ext/i18n/ \
    && chown -R tomcat:tomcat /opt/banner/extensions

COPY --chown=tomcat:tomcat  StudentRegistrationSsb /usr/local/tomcat/webapps/StudentRegistrationSsb
