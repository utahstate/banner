FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.72-jdk8-corretto

LABEL version="9.18"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
