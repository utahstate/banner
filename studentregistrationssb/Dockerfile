FROM harbor.usu.edu/banner/base-bcm:tomcat8.5.81-jre8-corretto

LABEL version="9.26.0.1"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone


RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ \
    && mkdir -p /opt/banner/extensions/ss_ext/i18n/ \
    && chown -R tomcat:tomcat /opt/banner/extensions

USER tomcat

COPY --chown=tomcat:tomcat  StudentRegistrationSsb /usr/local/tomcat/webapps/StudentRegistrationSsb
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/StudentRegistrationSsb/saml
