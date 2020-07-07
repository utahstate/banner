FROM edurepo/banner9-admin:tomcat8.5.56-corretto8
LABEL banner.common="9.3.21.0.5" \
      banner.accountreceivable="9.3.15.0.2" \
      banner.finance="9.3.16.0.4" \
      banner.financialaid="9.3.21.0.1" \
      banner.general="9.3.17.1.1" \
      banner.humanresources="9.3.15.0.4" \
      banner.positioncontrol="9.3.13.0.2" \
      banner.student="9.3.19.0.4" 

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat


COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
