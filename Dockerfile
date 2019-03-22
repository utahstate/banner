FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false \
    TIMEZONE=America\Denver

RUN sed -i 's/shared.loader=.*/shared.loader=xom-*.jar, bcprov*.jar/' /usr/local/tomcat/conf/catalina.properties
COPY ojdbc7.jar /usr/local/tomcat/lib/ojdbc7.jar
COPY --chown=tomcat:tomcat  BannerGeneralSsb /usr/local/tomcat/webapps/BannerGeneralSsb
