FROM harbor.usu.edu/banner/base-bep:tomcat8.5.38-jre8-alpine

LABEL version="9.2"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerEventPublisher /usr/local/tomcat/webapps/BannerEventPublisher
