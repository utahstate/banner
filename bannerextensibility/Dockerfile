FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts
LABEL version="9.15" 

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
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/BannerExtensibility/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat


COPY --chown=tomcat:tomcat BannerExtensibility /usr/local/tomcat/webapps/BannerExtensibility
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/BannerExtensibility/saml
