FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto


LABEL financeselfservice=9.5

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/FinanceSelfService/WEB-INF/classes/banner_configuration.groovy"
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender
USER tomcat

COPY --chown=tomcat:tomcat FinanceSelfService /usr/local/tomcat/webapps/FinanceSelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/FinanceSelfService/saml
