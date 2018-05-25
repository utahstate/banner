FROM edurepo/banner9-selfservice:tomcat8-jre8-alpine
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

LABEL financeselfservice=9.1.1

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false \
    TIMEZONE=America\Denver

COPY FinanceSelfService /usr/local/tomcat/webapps/FinanceSelfService
