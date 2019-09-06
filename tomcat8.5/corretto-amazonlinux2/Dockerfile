FROM tomcat:8.5.45-jdk8-corretto

ENV TIMEZONE="America/New_York"
ENV XMS=2g XMX=4g BANNERDB_JDBC=jdbc:oracle:thin:@//oracle.example.edu:1521/prod \
  MAXMETASPACE=512m \
  BANPROXY_USERNAME=banproxy \
  BANPROXY_INITALSIZE=25 \
  BANPROXY_MAXTOTAL=400 \
  BANPROXY_MAXACTIVE=300 \
  BANPROXY_MAXIDLE=-1 \
  BANPROXY_MAXWAIT=30000 \
  BANSSUSER_USERNAME=ban_ss_user \
  BANSSUSER_INITALSIZE=25 \
  BANSSUSER_MAXTOTAL=400 \
  BANSSUSER_MAXACTIVE=300 \
  BANSSUSER_MAXIDLE=-1 \
  BANSSUSER_MAXWAIT=30000 \
  REMOVE_ABANDONED_ON_MAINTENANCE=true \
  REMOVE_ABANDONED_ON_BORROW=true \
  REMOVE_ABANDONED_TIMEOUT=2100 \
  LOG_ABANDONED=true \
  DEFAULT_ROW_PREFETCH=150

ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false"

RUN yum update -y \
    && amazon-linux-extras install epel \
    && yum install -y xmlstarlet shadow-utils \
    && groupadd -r tomcat && useradd -r -g tomcat tomcat

#switched to using local ojdbc8.jar and xdb8.jar. Unable to find a good download. 
ADD ojdbc8.jar /usr/local/tomcat/lib/ojdbc8.jar
ADD xdb6.jar /usr/local/tomcat/lib/xdb6.jar

RUN cd /usr/local/tomcat/lib/ \
  && chown tomcat:tomcat ojdbc8.jar xdb6.jar && chmod +r ojdbc8.jar xdb6.jar


COPY context.xml /usr/local/tomcat/conf/context.xml
COPY server.xml /usr/local/tomcat/conf/server.xml
COPY run.sh /usr/local/tomcat/bin

# Configure logging to forward tomcat logs to docker log collector
COPY logging.properties /usr/local/tomcat/conf/logging.properties
RUN ln -s /dev/stderr /usr/local/tomcat/logs/localhost.log
RUN ln -s /dev/stderr /usr/local/tomcat/logs/stacktrace.log

RUN mkdir -p /opt/banner/config && mkdir -p /opt/banner/images && chown -R tomcat:tomcat /opt/banner

RUN chown -R tomcat:tomcat /usr/local/tomcat && chmod +x /usr/local/tomcat/bin/run.sh

EXPOSE 8080
USER tomcat
CMD ["bin/run.sh"]
