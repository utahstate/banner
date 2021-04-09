FROM edurepo/banner9-selfservice:tomcat8.5.57-jre8-corretto
LABEL MAINTAINER="Ken Andersen <ken.andersen@usu.edu>"

LABEL version="9.16"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat StudentSelfService /usr/local/tomcat/webapps/StudentSelfService
