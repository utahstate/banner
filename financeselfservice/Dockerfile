FROM usuit/banner:base-bannerselfservice-9.0.71-jdk8-corretto


LABEL financeselfservice=9.4.7.2

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender
USER tomcat

COPY --chown=tomcat:tomcat FinanceSelfService /usr/local/tomcat/webapps/FinanceSelfService
COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/FinanceSelfService/saml
