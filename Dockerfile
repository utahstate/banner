FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
LABEL MAINTAINER="Eric Allen <eric.allen@usu.edu>"

ENV TIMEZONE=America/Denver \
    BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false

COPY --chown=tomcat:tomcat StudentSelfService /usr/local/tomcat/webapps/StudentSelfService
