FROM tomcat:8.5-jre8-alpine

ENV TIMEZONE="America/Denver" \
    STUDENTAPI_URL="https://localhost/StudentApi/api" \
    INTEGRATIONAPI_URL="https://localhost/IntegrationApi/api" \
    API_USERNAME="user" \
    API_PASSWORD="password" \
    RABBITMQ_USERNAME="ellucian" \
    RABBITMQ_PASSWORD="rabbit" \
    ETHOS_STUDENT_KEY="key" \
    ETHOS_INTEGRATION_KEY="key" \
    EMA_CONFIG="secret"

RUN rm -Rf /usr/local/tomcat/webapps/* \
    && apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/$TIMEZONE /etc/localtime

COPY EllucianMessagingAdapter /usr/local/tomcat/webapps/EllucianMessagingAdapter

COPY run.sh /usr/local/tomcat/bin
RUN chmod +x /usr/local/tomcat/bin/run.sh
CMD ["bin/run.sh"]
