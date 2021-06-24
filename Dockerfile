FROM harbor.usu.edu/banner/base-banneradmin:8.5.65-jdk8-corretto
LABEL banner.common="9.3.25.0.6" \
      banner.accountreceivable="9.3.19.0.5" \
      banner.finance="9.3.21.0.3" \
      banner.financialaid="9.3.26.0.4" \
      banner.general="9.3.20.0.5" \
      banner.humanresources="9.3.19.0.4" \
      banner.positioncontrol="9.3.14.0.1" \
      banner.student="9.3.24.0.5" \
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
