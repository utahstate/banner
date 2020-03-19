FROM edurepo/banner9-selfservice:tomcat8.5.46-jre8-corretto
LABEL version="9.5.0.1" \
  maintainer="Eric Allen <eric.allen@usu.edu>"

USER root
RUN mkdir -p /opt/banner/extensibility/pb \
  && mkdir -p /opt/banner/extensibility/pb/i18n \
  && mkdir -p /opt/banner/extensibility/pb/page \
  && mkdir -p /opt/banner/extensibility/pb/css \
  && mkdir -p /opt/banner/extensibility/pb/virtdom \
  && mkdir -p /opt/banner/extensibility/themes \
  && chown -R tomcat:tomcat /opt/banner/extensibility

VOLUME /opt/banner/extensibility
USER tomcat

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat


COPY --chown=tomcat:tomcat BannerExtensibility /usr/local/tomcat/webapps/BannerExtensibility
