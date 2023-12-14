FROM usuit/banner:base-bep-9.0.84-jdk8-corretto-cacerts

LABEL version="9.12"

# Fix timezone
USER root
ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/BannerEventPublisher/WEB-INF/classes/banner_configuration.groovy"
ENV TIMEZONE=America/Denver
RUN cp -f /usr/share/zoneinfo/$TIMEZONE /etc/localtime
RUN echo $TIMEZONE > /etc/timezone
USER tomcat

COPY --chown=tomcat:tomcat BannerEventPublisher /usr/local/tomcat/webapps/BannerEventPublisher
