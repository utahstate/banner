FROM harbor.usu.edu/banner/base-bep:tomcat8.5.81-jre8-corretto

LABEL version="9.9.0.1"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerEventPublisher /usr/local/tomcat/webapps/BannerEventPublisher
