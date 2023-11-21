FROM usuit/banner:base-banneradmin-9.0.80-jdk8-corretto
LABEL banner.common="9.3.32.0.4" \
      banner.accountreceivable="9.3.25.2.1" \
      banner.finance="9.3.29.0.5" \
      banner.financialaid="9.3.37.0.5" \
      banner.general="9.3.28.0.3" \
      banner.humanresources="9.3.28.0.4" \
      banner.positioncontrol="9.3.22.0.4" \
      banner.student="9.3.31.1.1" \
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
