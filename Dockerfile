FROM harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat7-java7
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

USER root
RUN mkdir -p /opt/xe/extensibility/pb \
  && mkdir -p /opt/xe/extensibility/pb/i18n \
  && mkdir -p /opt/xe/extensibility/pb/page \
  && mkdir -p /opt/xe/extensibility/pb/css \
  && mkdir -p /opt/xe/extensibility/pb/virtdom \
  && mkdir -p /opt/xe/extensibility/themes \
  && chown -R tomcat:tomcat /opt/xe/extensibility

VOLUME /opt/xe/extensibility
USER tomcat

ENV JAVA_OPTS -Duser.timezone=\$TIMEZONE \
    -Dlog4j.configuration=config.properties -Xms\$XMS -Xmx\$XMX \
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

COPY BannerExtensibility.war /usr/local/tomcat/webapps/BannerExtensibility.war
