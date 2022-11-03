FROM usuit/banner:base-banneradmin-8.5.81-jdk8-corretto
LABEL banner.common="9.3.29.1.1" \
      banner.accountreceivable="9.3.22.2.2" \
      banner.finance="9.3.25.0.6" \
      banner.financialaid="9.3.31.0.2" \
      banner.general="9.3.24.0.4" \
      banner.humanresources="9.3.23.0.6" \
      banner.positioncontrol="9.3.17.0.4" \
      banner.student="9.3.28.1.2" \
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
COPY --chown=tomcat:tomcat applicationContext.xml.saml /usr/local/tomcat/webapps/BannerAdmin.ws/WEB-INF/applicationContext.xml.saml
#COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
