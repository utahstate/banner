FROM harbor.usu.edu/banner/base-banneradmin:8.5.81-jdk8-corretto
LABEL banner.access.management=9.3.18.1.2

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
RUN mkdir /u01
COPY saml /u01/saml
USER tomcat

COPY --chown=tomcat:tomcat BannerAccessMgmt /usr/local/tomcat/webapps/BannerAccessMgmt
COPY --chown=tomcat:tomcat BannerAccessMgmt.ws /usr/local/tomcat/webapps/BannerAccessMgmt.ws
COPY --chown=tomcat:tomcat run.sh /usr/local/tomcat/bin
RUN chmod +x /usr/local/tomcat/bin/run.sh
