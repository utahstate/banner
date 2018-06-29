FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
LABEL version="9.2" \
  maintainer="Eric Allen <eric.allen@usu.edu>"

USER root
RUN mkdir -p /opt/banner/extensibility/pb \
  && mkdir -p /opt/banner/extensibility/pb/i18n \
  && mkdir -p /opt/banner/extensibility/pb/page \
  && mkdir -p /opt/banner/extensibility/pb/css \
  && mkdir -p /opt/banner/extensibility/pb/virtdom \
  && mkdir -p /opt/banner/extensibility/themes \
  && chown -R tomcat:tomcat /opt/banner/extensibility

VOLUME /opt/xe/extensibility
USER tomcat

ENV TIMEZONE=America/Denver \
    BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false


COPY --chown=tomcat:tomcat BannerExtensibility /usr/local/tomcat/webapps/BannerExtensibility
