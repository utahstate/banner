FROM harbor.usu.edu/banner/base-bcm:tomcat8.5.38-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL version=9.3

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

RUN sed -i 's/shared.loader=.*/shared.loader=xom-*.jar, bcprov*.jar/' /usr/local/tomcat/conf/catalina.properties
COPY ojdbc7.jar /usr/local/tomcat/lib/ojdbc7.jar
COPY --chown=tomcat:tomcat  BannerGeneralSsb /usr/local/tomcat/webapps/BannerGeneralSsb
