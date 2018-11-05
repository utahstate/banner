FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL financeselfservice=9.2

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat FinanceSelfService /usr/local/tomcat/webapps/FinanceSelfService
