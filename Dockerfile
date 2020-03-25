FROM edurepo/banner9-admin:tomcat8.5.47-corretto8
LABEL banner.common="9.3.18.3.2" \
      banner.accountreceivable="9.3.13.0.2" \
      banner.documentmanagement="9.3.1.0.4" \
      banner.finance="9.3.13.0.5" \
      banner.financialaid="9.3.18.0.2" \
      banner.general="9.3.15.0.6" \
      banner.humanresources="9.3.13.0.2" \
      banner.positioncontrol="9.3.12.0.3" \
      banner.student="9.3.16.0.4" 

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat


COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
