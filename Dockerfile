FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.81-jdk8-corretto

LABEL version="9.19"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/EmployeeSelfService/saml
