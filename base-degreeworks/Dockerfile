#Dockerfile for bannerselfservice Java 8 & tomcat 8
FROM tomcat:8.5.41-jre8-alpine

ENV TIMEZONE="America/New_York"


ENV XMS=2048m \
    XMX=4g \
    DW_INITIALSIZE=10 \
    DW_MAXACTIVE=100 \
    DW_MINIDLE=10 \
    DW_MAXIDLE=10 \
    DW_MAXWAIT=3000 \
    DW_JDBC=jdbc:oracle:thin:@dbhost.mycollege.edu:1521:sid \
    DW_USERNAME=username \
    DW_PASSWORD=password

ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -Duser.timezone=\$TIMEZONE"

RUN apk add --no-cache xmlstarlet \
    && rm -Rf /usr/local/tomcat/webapps/* \
    && apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/$TIMEZONE /etc/localtime \
    && addgroup -S tomcat \
    && adduser -S -G tomcat -h /usr/local/tomcat tomcat

COPY java/ /usr/local/tomcat/lib/
COPY context.xml /usr/local/tomcat/conf/context.xml
COPY server.xml /usr/local/tomcat/conf/server.xml
COPY run.sh /usr/local/tomcat/bin/run.sh

RUN chown -R tomcat:tomcat /usr/local/tomcat && chmod +x /usr/local/tomcat/bin/run.sh
EXPOSE 8080
USER tomcat
CMD ["bin/run.sh"]
