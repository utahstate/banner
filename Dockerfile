FROM edurepo/banner9-selfservice:tomcat8.5.46-jre8-corretto

LABEL version="3.2.0.1"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat applicationNavigator /usr/local/tomcat/webapps/applicationNavigator
