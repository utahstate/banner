FROM usuit/banner:base-bcm-9.0.71-jdk8-corretto

LABEL version="9.28"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/StudentRegistrationSsb/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone


RUN mkdir -p /opt/banner/extensions/ss_ext/extensions/ \
    && mkdir -p /opt/banner/extensions/ss_ext/i18n/ \
    && chown -R tomcat:tomcat /opt/banner/extensions

USER tomcat

COPY --chown=tomcat:tomcat  StudentRegistrationSsb /usr/local/tomcat/webapps/StudentRegistrationSsb
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/StudentRegistrationSsb/saml
