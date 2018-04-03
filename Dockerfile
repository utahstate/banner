FROM edurepo/banner9-admin:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV TIMEZONE=America/Denver

COPY BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY bannerHelp /usr/local/tomcat/webapps/bannerHelp

#COPY BannerAdmin.war /usr/local/tomcat/webapps/BannerAdmin.war
#COPY BannerAdmin.ws.war /usr/local/tomcat/webapps/BannerAdmin.ws.war
#COPY bannerHelp.war /usr/local/tomcat/webapps/bannerHelp.war
