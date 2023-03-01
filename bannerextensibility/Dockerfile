FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto
LABEL version="9.11.0.3" 

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
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/BannerExtensibility/saml
