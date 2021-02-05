FROM edurepo/banner9-admin:tomcat8.5.58-corretto8
LABEL banner.common="9.3.23.1.1" \
      banner.accountreceivable="9.3.17.0.2" \
      banner.finance="9.3.18.0.3" \
      banner.financialaid="9.3.23.0.1" \
      banner.general="9.3.18.0.4" \
      banner.humanresources="9.3.17.0.3" \
      banner.positioncontrol="9.3.13.0.2" \
      banner.student="9.3.22.0.4" \
      cleanaddress="4.4.5"

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
#COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
