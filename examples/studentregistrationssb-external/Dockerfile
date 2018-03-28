FROM banner9-selfservice:jre8-alpine

ENV TIMEZONE="America/New_York"

COPY config /opt/banner/config


ENV JAVA_OPTS="-dBANNER_APP_CONFIG=/opt/banner/config/banner_configuration.groovy -dBANNER_STUDENT_REGISTRATION_SSB_CONFIG=/opt/banner/config/StudentRegistrationSsb_configuration.groovy"

COPY StudentRegistrationSsb /usr/local/tomcat/webapps/StudentRegistrationSsb
