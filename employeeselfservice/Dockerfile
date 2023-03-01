FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto

LABEL version="9.21.0.1"

ENV TIMEZONE=America/Denver

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/EmployeeSelfService/META-INF/context.xml
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/EmployeeSelfService/saml
