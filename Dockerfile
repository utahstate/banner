FROM harbor.usu.edu/banner/base-banneradmin:oraclelinux6-tomcat7-java7
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV CATALINA_HOME /usr/local/tomcat

ENV JAVA_OPTS -Dlog4j.configuration=config.properties -Xms2048m -Xmx4g \
    -XX:MaxPermSize=1024m -XX:+UseParallelGC \
    -Dbanproxy.jdbc.url=\$BANPROXY_JDBC_URL \
    -Dbanproxy.password=\$BANPROXY_PASSWORD \
    -Dbanproxy.initialsize=\$BANPROXY_INITIALSIZE \
    -Dbanproxy.maxactive=\$BANPROXY_MAXACTIVE \
    -Dbanproxy.maxidle=\$BANPROXY_MAXIDLE \
    -Dbanproxy.maxwait=\$BANPROXY_MAXWAIT \
    -Dlb.name=\$LB_NAME

COPY eTranscriptAPI-9.0.0.4.war /usr/local/tomcat/webapps/eTranscriptAPI.war
COPY ojdbc7.jar /usr/local/tomcat/lib/ojdbc6.jar
