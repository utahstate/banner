FROM usuit/banner:base-banneradmin-9.0.71-jdk8-corretto
<<<<<<< HEAD
LABEL banner.common="9.3.31.2.2" \
      banner.accountreceivable="9.3.24.1.2" \
      banner.finance="9.3.28.0.2" \
      banner.financialaid="9.3.36.0.7" \
      banner.general="9.3.27.0.2" \
      banner.humanresources="9.3.27.0.1" \
      banner.positioncontrol="9.3.21.0.1" \
      banner.student="9.3.30.2.4" \
||||||| bda9048
LABEL banner.common="9.3.30.0.8" \
      banner.accountreceivable="9.3.23.0.1" \
      banner.finance="9.3.26.0.3" \
      banner.financialaid="9.3.34.0.1" \
      banner.general="9.3.25.0.4" \
      banner.humanresources="9.3.25.0.1" \
      banner.positioncontrol="9.3.19.0.3" \
      banner.student="9.3.29.1.1" \
=======
LABEL banner.common="9.3.30.0.8" \
      banner.accountreceivable="9.3.23.0.1" \
      banner.finance="9.3.26.0.3" \
      banner.financialaid="9.3.36.0.7" \
      banner.general="9.3.25.0.4" \
      banner.humanresources="9.3.25.0.1" \
      banner.positioncontrol="9.3.19.0.3" \
      banner.student="9.3.29.1.1" \
>>>>>>> 8154ed14b3b5d3c7856c5f100931462c3c0b7f0f
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
