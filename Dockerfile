FROM edurepo/banner9-admin:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp

