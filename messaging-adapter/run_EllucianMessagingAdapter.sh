#!/bin/bash

cp -f /run/app-config/emsConfig.xml /usr/local/tomcat/webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
chmod 644 /usr/local/tomcat/webapps/EllucianMessagingAdapter/WEB-INF/emsConfig.xml
