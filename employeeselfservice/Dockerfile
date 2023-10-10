FROM usuit/banner:base-bannerselfservice-9.0.80-jdk8-corretto

LABEL version="9.24"

ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/EmployeeSelfService/WEB-INF/classes/banner_configuration.groovy"

COPY --chown=tomcat:tomcat EmployeeSelfService /usr/local/tomcat/webapps/EmployeeSelfService
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/EmployeeSelfService/META-INF/context.xml
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/EmployeeSelfService/saml
