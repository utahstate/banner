FROM usuit/banner:base-banneradmin-9.0.71-jdk8-corretto
LABEL banner.common="9.3.30.0.8" \
      banner.accountreceivable="9.3.23.0.1" \
      banner.finance="9.3.26.0.3" \
      banner.financialaid="9.3.36.0.7" \
      banner.general="9.3.25.0.4" \
      banner.humanresources="9.3.25.0.1" \
      banner.positioncontrol="9.3.19.0.3" \
      banner.student="9.3.29.1.1" \
      cleanaddress="4.4.7"

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
