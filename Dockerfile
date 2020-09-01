FROM edurepo/banner9-selfservice:tomcat8.5.57-jre8-corretto

LABEL version="9.12"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
