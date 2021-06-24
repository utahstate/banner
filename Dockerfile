FROM harbor.usu.edu/banner/base-bannerselfservice:8.5.65-jdk8-corretto

LABEL version="9.16.1"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat StudentSelfService /usr/local/tomcat/webapps/StudentSelfService
