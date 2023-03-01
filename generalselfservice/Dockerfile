FROM usuit/banner:base-bcm-9.0.71-jdk8-corretto


LABEL version="9.12.2.1"

# Fix timezone:
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat  BannerGeneralSsb /usr/local/tomcat/webapps/BannerGeneralSsb
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/BannerGeneralSsb/saml
