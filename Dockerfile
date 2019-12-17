FROM edurepo/banner9-selfservice:tomcat8.5.45-jre8-corretto

LABEL version="9.9"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
