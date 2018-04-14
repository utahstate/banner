FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false \
    TIMEZONE=America\Denver

COPY FacultySelfService /usr/local/tomcat/webapps/FacultySelfService