FROM edurepo/banner9-selfservice:tomcat8.5.46-jre8-corretto

LABEL version="9.11"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
