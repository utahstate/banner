FROM bandock/banner9-admin:tomcat8.5.37-jre8-alpine
LABEL banner.common="9.3.15.1.1" \
      banner.accountreceivable="9.3.9.0.4" \
      banner.documentmanagement="9.3.1.0.4" \
      banner.finance="9.3.19.0.3" \
      banner.financialaid="9.3.12.1.1" \
      banner.general="9.3.12.0.3" \
      banner.humanresources="9.3.9.1.2" \
      banner.positioncontrol="9.3.9.0.1" \
      banner.student="9.3.13.1.1" \
      cleanaddress="9.2.5"

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
