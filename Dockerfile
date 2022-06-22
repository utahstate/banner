FROM harbor.usu.edu/banner/base-banneradmin:8.5.72-jdk8-corretto
LABEL banner.common="9.3.27.0.3" \
      banner.accountreceivable="9.3.21.1.1" \
      banner.finance="9.3.23.0.6" \
      banner.financialaid="9.3.30.0.1" \
      banner.general="9.3.22.1.1" \
      banner.humanresources="9.3.21.2.1" \
      banner.positioncontrol="9.3.15.0.3" \
      banner.student="9.3.26.1.2" \
      cleanaddress="4.4.5"

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
RUN mkdir /u01
COPY saml /u01/saml
USER tomcat

COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
#COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
