FROM harbor.usu.edu/banner/base-bcm:tomcat8.5.81-jre8-corretto


LABEL version=9.12

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat  BannerGeneralSsb /usr/local/tomcat/webapps/BannerGeneralSsb
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/BannerGeneralSsb/saml
