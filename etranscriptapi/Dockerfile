FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts

#Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/eTranscriptAPI/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone

USER tomcat
COPY --chown=tomcat:tomcat eTranscriptAPI /usr/local/tomcat/webapps/eTranscriptAPI
#COPY --chown=tomcat:tomcat ojdbc7.jar /usr/local/tomcat/lib/ojdbc6.jar
