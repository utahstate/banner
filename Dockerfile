FROM harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat8-java8
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER = false

ENV JAVA_OPTS -Duser.timezone=\$TIMEZONE \
    -Xms\$XMS -Xmx\$XMX \
    -XX:MaxPermSize=\$MAXPERMSIZE -Dbanproxy.jdbc.url=\$BANPROXY_JDBC_URL \
    -Dbanproxy.password=\$BANPROXY_PASSWORD \
    -Dbanproxy.initialsize=\$BANPROXY_INITIALSIZE \
    -Dbanproxy.maxactive=\$BANPROXY_MAXACTIVE \
    -Dbanproxy.maxidle=\$BANPROXY_MAXIDLE \
    -Dbanproxy.maxwait=\$BANPROXY_MAXWAIT \
    -Dbanssuser.jdbc.url=\$BANSSUSER_JDBC_URL \
    -Dbanssuser.password=\$BANSSUSER_PASSWORD \
    -Dbanssuser.initialsize=\$BANSSUSER_INITIALSIZE \
    -Dbanssuser.maxactive=\$BANSSUSER_MAXACTIVE \
    -Dbanssuser.maxidle=\$BANSSUSER_MAXIDLE \
    -Dbanssuser.maxwait=\$BANSSUSER_MAXACTIVE \
    -Dcas.serverurlprefix=\$GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX \
    -Dserver.name=\$GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME

COPY StudentRegistrationSsb.war /usr/local/tomcat/webapps/StudentRegistrationSsb.war
