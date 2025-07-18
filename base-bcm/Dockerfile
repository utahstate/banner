FROM tomcat:9-jdk17-corretto

ENV TIMEZONE="America/New_York"
ENV XMS=2g XMX=4g BANNERDB_JDBC=jdbc:oracle:thin:@//oracle.example.edu:1521/prod \
  MAXMETASPACE=512m \
  BANPROXY_USERNAME=banproxy \
  BANPROXY_INITALSIZE=25 \
  BANPROXY_MAXTOTAL=400 \
  BANPROXY_MAXIDLE=-1 \
  BANPROXY_MAXWAIT=30000 \
  BANSSUSER_USERNAME=ban_ss_user \
  BANSSUSER_INITALSIZE=25 \
  BANSSUSER_MAXTOTAL=400 \
  BANSSUSER_MAXIDLE=-1 \
  BANSSUSER_MAXWAIT=30000 \
  REMOVE_ABANDONED_ON_MAINTENANCE=true \
  REMOVE_ABANDONED_ON_BORROW=true \
  REMOVE_ABANDONED_TIMEOUT=2100 \
  LOG_ABANDONED=true \
  DEFAULT_ROW_PREFETCH=150 \
  COMMMGR_USERNAME=commmgr \
  COMMMGR_INITALSIZE=25 \
  COMMMGR_MAXTOTAL=400 \
  COMMMGR_MAXIDLE=-1 \
  COMMMGR_MAXWAIT=30000 \
  REMOVE_ABANDONED_ON_MAINTENANCE=true \
  REMOVE_ABANDONED_ON_BORROW=true \
  REMOVE_ABANDONED_TIMEOUT=2100 \
  LOGGING_DIR=/usr/local/tomcat/logs

ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false -Dbanner.logging.dir=\$LOGGING_DIR"

RUN yum update -y \
    && amazon-linux-extras install epel \
    && yum install -y xmlstarlet shadow-utils \
    && rm -Rf /usr/local/tomcat/webapps/* \
    && cp /usr/share/zoneinfo/$TIMEZONE /etc/localtime \
    && groupadd -r tomcat && useradd -r -g tomcat tomcat

#switched to using local ojdbc8.jar and xdb8.jar. Unable to find a good download. 
ADD ojdbc8.jar /usr/local/tomcat/lib/ojdbc8.jar
#ADD ojdbc11.jar /usr/local/tomcat/lib/ojdbc11.jar
ADD xdb.jar /usr/local/tomcat/lib/xdb.jar
ADD ucp.jar /usr/local/tomcat/lib/ucp.jar

RUN cd /usr/local/tomcat/lib/ \
  && chown tomcat:tomcat ojdbc8.jar xdb.jar && chmod +r ojdbc8.jar xdb.jar ucp.jar


COPY context.xml /usr/local/tomcat/conf/context.xml
COPY server.xml /usr/local/tomcat/conf/server.xml
COPY run.sh /usr/local/tomcat/bin

RUN mkdir -p /etc/certs

COPY USERTrust_RSA_Certification_Authority.cer /etc/certs/USERTrust_RSA_Certification_Authority.cer
COPY InCommon_RSA_Server_CA_2.cer /etc/certs/InCommon_RSA_Server_CA_2.cer 

RUN keytool -import -alias usertrust -file /etc/certs/USERTrust_RSA_Certification_Authority.cer -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt
RUN keytool -import -alias incommon -file /etc/certs/InCommon_RSA_Server_CA_2.cer -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt


# Configure logging to forward tomcat logs to docker log collector
COPY logging.properties /usr/local/tomcat/conf/logging.properties
RUN ln -s /dev/stderr /usr/local/tomcat/logs/localhost.log
RUN ln -s /dev/stderr /usr/local/tomcat/logs/stacktrace.log

RUN chown -R tomcat:tomcat /usr/local/tomcat && chmod +x /usr/local/tomcat/bin/run.sh

EXPOSE 8080
USER tomcat
CMD ["bin/run.sh"]
