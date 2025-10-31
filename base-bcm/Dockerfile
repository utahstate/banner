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
    LOGGING_DIR=/usr/local/tomcat/logs \
    CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false -Dbanner.logging.dir=\$LOGGING_DIR"

RUN    yum update -y \
    && amazon-linux-extras install epel \
    && yum install -y xmlstarlet shadow-utils \
    && rm -Rf /usr/local/tomcat/webapps/* \
    && cp /usr/share/zoneinfo/$TIMEZONE /etc/localtime \
    && groupadd -r tomcat && useradd -r -g tomcat tomcat \
# Clear the package cache we just generated so its never stored in a layer (this should be a pretty significant image size reduction).
    && yum clean all

# cert management must be done as root
COPY --chown=tomcat:tomcat USERTrust_RSA_Certification_Authority.cer InCommon_RSA_Server_CA_2.cer /etc/certs/
RUN    keytool -import -alias usertrust -file /etc/certs/USERTrust_RSA_Certification_Authority.cer -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt \
    && keytool -import -alias incommon -file /etc/certs/InCommon_RSA_Server_CA_2.cer -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt

# Move USER directive earlier so things are owned by tomcat user.
USER tomcat

# Reduce layers created by multiple COPY directives
COPY ojdbc8.jar xdb.jar ucp.jar /usr/local/tomcat/lib/
COPY context.xml server.xml logging.properties /usr/local/tomcat/conf/
COPY run.sh /usr/local/tomcat/bin

# Configure logging to forward tomcat logs to docker log collector (reduced to single layer)
RUN    ln -s /dev/stderr /usr/local/tomcat/logs/localhost.log \
    && ln -s /dev/stderr /usr/local/tomcat/logs/stacktrace.log

EXPOSE 8080
CMD ["bin/run.sh"]
