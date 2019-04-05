#!/bin/sh

#Fail fast
set -e

sed -i "s|rabbitmq.username|$RABBITMQ_USERNAME|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
sed -i "s|rabbitmq.password|$RABBITMQ_PASSWORD|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml

sed -i "s|api.student.url|$STUDENTAPI_URL|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
sed -i "s|api.username|$API_USERNAME|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
sed -i "s|api.password|$API_PASSWORD|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
sed -i "s|ethos.student.key|$ETHOS_STUDENT_KEY|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
sed -i "s|api.integration.url|$INTEGRATIONAPI_URL|" webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
sed -i "s|ethos.integration.key|$ETHOS_INTEGRATION_KEY|"  webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml


catalina.sh run