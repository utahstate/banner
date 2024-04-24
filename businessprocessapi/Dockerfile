FROM usuit/banner:base-banneradmin-9.0.87-jdk8-corretto-cacerts

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
COPY run.sh /usr/local/tomcat/bin
RUN chown -R tomcat:tomcat /usr/local/tomcat && chmod +x /usr/local/tomcat/bin/run.sh
USER tomcat

COPY --chown=tomcat:tomcat BannerAdminBPAPI /usr/local/tomcat/webapps/BannerAdminBPAPI
COPY --chown=tomcat:tomcat BannerAdminBPAPI_configs /usr/local/tomcat/webapps/BannerAdminBPAPI
COPY --chown=tomcat:tomcat BannerAdminBPAPI_configs/config /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/config
COPY --chown=tomcat:tomcat BannerAdminBPAPI_configs/config/* /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/
COPY --chown=tomcat:tomcat applicationContext.xml /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/
