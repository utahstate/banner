FROM bandock/banner9-admin:tomcat8.5.41-jre8-alpine
LABEL banner.common="9.3.17.0.5" \
      banner.accountreceivable="9.3.11.0.4" \
      banner.documentmanagement="9.3.1.0.4" \
      banner.finance="9.3.12.0.4" \
      banner.financialaid="9.3.14.1.1" \
      banner.general="9.3.14.0.4" \
      banner.humanresources="9.3.11.0.3" \
      banner.positioncontrol="9.3.11.0.3" \
      banner.student="9.3.15.2.1" \
      cleanaddress="9.3.17"

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

RUN sed -i 's/shared.loader=.*/shared.loader=xom-*.jar, bcprov*.jar/' /usr/local/tomcat/conf/catalina.properties
COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
