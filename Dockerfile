FROM harbor.usu.edu/banner/base-banneradmin:oraclelinux6-tomcat8-java8
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV CAS_SERVER https://logindev.usu.edu/cas \
  SERVER_URL https://admin-zdevl.banner.usu.edu:443 \
  THEME_URL /wrksp.branding/css/all.css \
  THEME_ENABLE true \
  DATABASE DEVL

ENV JAVA_OPTS -Duser.timezone=\$TIMEZONE -XX:+UseParallelGC\
    -Dlog4j.configuration=config.properties -Xms\$XMS -Xmx\$XMX \
    -Dbanproxy.jdbc.url=\$BANPROXY_JDBC_URL \
    -Dbanproxy.password=\$BANPROXY_PASSWORD \
    -Dbanproxy.initialsize=\$BANPROXY_INITIALSIZE \
    -Dbanproxy.maxactive=\$BANPROXY_MAXACTIVE \
    -Dbanproxy.maxidle=\$BANPROXY_MAXIDLE \
    -Dbanproxy.maxwait=\$BANPROXY_MAXWAIT \
    -DlogFileDir=\$LOGFILEDIR \
    -Dlb.name=\$LB_NAME \
    -Dcas.server.location=\$CAS_SERVER \
    -Dserver.url=\$SERVER_URL \
    -Dtheme.url=\$THEME_URL \
    -Dtheme.enable=\$THEME_ENABLE \
    -Dconnection.database=$\DATABASE

COPY BannerAdmin.war /usr/local/tomcat/webapps/BannerAdmin.war
COPY BannerAdmin.ws.war /usr/local/tomcat/webapps/BannerAdmin.ws.war
COPY bannerHelp.war /usr/local/tomcat/webapps/bannerHelp.war
