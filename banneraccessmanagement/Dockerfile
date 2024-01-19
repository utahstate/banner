FROM usuit/banner:base-banneradmin-9.0.84-jdk8-corretto-cacerts
LABEL banner.access.management=9.3.25.0.4

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
COPY --chown=tomcat:tomcat applicationContext.xml.saml /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/
COPY --chown=tomcat:tomcat saml/aws/aws-bam* /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/
COPY --chown=tomcat:tomcat saml/zpprd/zpprd-bam* /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/
COPY --chown=tomcat:tomcat saml/zdevl/zdevl-bam* /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/
COPY --chown=tomcat:tomcat saml/zprod/zprod-bam* /usr/local/tomcat/webapps/BannerAccessMgmt.ws/WEB-INF/classes/
RUN chmod +x /usr/local/tomcat/bin/run.sh
