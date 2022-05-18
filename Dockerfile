FROM tomcat:8.5.72-jdk8-corretto

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

RUN yum update -y \
    && amazon-linux-extras install epel \
    && yum install -y xmlstarlet shadow-utils \
    && rm -Rf /usr/local/tomcat/webapps/* \
    && cp /usr/share/zoneinfo/$TIMEZONE /etc/localtime \
    && groupadd -r tomcat && useradd -r -g tomcat tomcat

RUN echo $TIMEZONE > /etc/timezone

COPY run.sh /usr/local/tomcat/bin

RUN chown -R tomcat:tomcat /usr/local/tomcat && chmod +x /usr/local/tomcat/bin/run.sh

USER tomcat

COPY --chown=tomcat:tomcat EllucianMessagingAdapter /usr/local/tomcat/webapps/EllucianMessagingAdapter

CMD ["bin/run.sh"]
