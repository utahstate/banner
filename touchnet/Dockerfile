FROM banner-base:build-jdk21-latest AS build
ARG VERSION

ADD https://download.oracle.com/otn_software/java/ords/ords-${VERSION}.zip /opt/build/ords.zip

RUN unzip ords.zip -d ./ords

FROM banner-base:9-jdk21-tomcat9 AS final

ARG VERSION

ENV APP_NAME="ords" APP_TYPE="ords"
LABEL edu.usu.banner.version=${VERSION} edu.usu.banner.appName="touchnet"

RUN mkdir -p /u01/app/tomcat/touchnet /u01/app/tomcat/touchnet_logs /u01/app/tomcat/touchnet_config
COPY --chown=tomcat:tomcat --from=build /opt/build/ords /u01/app/tomcat/touchnet
COPY --chown=tomcat:tomcat --from=build /opt/build/ords/ords.war /usr/local/tomcat/webapps/ords.war

# App-specific environment
ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false -Dbanner.logging.dir=\$LOGGING_DIR -Dconfig.url=\$ORDS_CONFIG" \
		ORDS_HOME=/u01/app/tomcat/touchnet \
		ORDS_LOGS=/u01/app/tomcat/touchnet_logs \
		ORDS_CONFIG=/u01/app/tomcat/touchnet_config

VOLUME /u01/app/tomcat/touchnet_config
