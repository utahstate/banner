FROM harbor.usu.edu/banner/base-bep:tomcat8.5.72-jre8-corretto

LABEL version="9.7"

# Fix timezone
USER root
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerEventPublisher /usr/local/tomcat/webapps/BannerEventPublisher
