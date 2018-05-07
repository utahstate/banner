FROM edurepo/banner9-admin:tomcat8-jre8-alpine
LABEL banner.common="9.3.12.0.3" \
      banner.accountreceivable="9.3.6.1.2" \
      banner.documentmanagement="9.3.0.29" \
      banner.finance="9.3.7.0.2" \
      banner.financialaid="9.3.8.0.3" \
      banner.general="9.3.9.0.4" \
      banner.humanresources="9.3.6.0.3" \
      banner.positioncontrol="9.3.6.0.3" \
      banner.student="9.3.10.0.6"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp

