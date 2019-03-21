FROM bandock/banner9-admin:tomcat8.5.37-jre8-alpine
LABEL banner.common="9.3.16.0.3" \
      banner.accountreceivable="9.3.10.0.4" \
      banner.documentmanagement="9.3.1.0.4" \
      banner.finance="9.3.11.0.2" \
      banner.financialaid="9.3.12.1.1" \
      banner.general="9.3.13.0.2" \
      banner.humanresources="9.3.10.0.4" \
      banner.positioncontrol="9.3.10.0.2" \
      banner.student="9.3.14.0.3"

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
