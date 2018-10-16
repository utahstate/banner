FROM edurepo/banner9-admin:tomcat8.5-jre8-alpine
LABEL banner.common="9.3.14.0.4" \
      banner.accountreceivable="9.3.8.0.4" \
      banner.documentmanagement="9.3.1.0.4" \
      banner.finance="9.3.9.0.2" \
      banner.financialaid="9.3.11.0.1" \
      banner.general="9.3.11.0.2" \
      banner.humanresources="9.3.8.0.2" \
      banner.positioncontrol="9.3.8.0.2" \
      banner.student="9.3.12.0.5"

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp

