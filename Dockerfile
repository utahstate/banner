FROM harbor.usu.edu/banner/base-bep:tomcat8.5.54-jre8-corretto-2

LABEL version="9.3"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerEventPublisher /usr/local/tomcat/webapps/BannerEventPublisher
