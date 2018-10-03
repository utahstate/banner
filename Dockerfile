FROM edurepo/banner9-selfservice:tomcat8.5-jre8-alpine

LABEL version="9.8"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
