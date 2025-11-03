FROM tomcat:9-jdk17-corretto

LABEL edu.usu.banner.image="base"

ENV TIMEZONE="America/New_York" \
    XMS=2g XMX=4g BANNERDB_JDBC=jdbc:oracle:thin:@//oracle.example.edu:1521/prod \
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
    LOGGING_DIR=/usr/local/tomcat/logs \
    CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false -Dbanner.logging.dir=\$LOGGING_DIR"

# Run the updates with /var/cache as a cache mount (so not contained in the image)
RUN --mount=type=cache,target=/var/cache \
    yum update -y \
    && amazon-linux-extras install epel \
    && yum install -y xmlstarlet shadow-utils \
    && rm -Rf /usr/local/tomcat/webapps/* \
    && cp /usr/share/zoneinfo/$TIMEZONE /etc/localtime \
    && groupadd -r tomcat && useradd -r -g tomcat tomcat

# cert management must be done as root
COPY USERTrust_RSA_Certification_Authority.cer InCommon_RSA_Server_CA_2.cer /etc/certs/
RUN    keytool -import -alias usertrust -file /etc/certs/USERTrust_RSA_Certification_Authority.cer -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt \
    && keytool -import -alias incommon  -file /etc/certs/InCommon_RSA_Server_CA_2.cer              -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt

# Change ownership of tomcat files
RUN chown -R tomcat:tomcat /usr/local/tomcat

# Move USER directive earlier so things are owned by tomcat user.
USER tomcat

# Reduce layers created by multiple COPY directives
COPY --chmod=644 ojdbc8.jar xdb.jar ucp.jar /usr/local/tomcat/lib/
COPY context.xml server.xml logging.properties /usr/local/tomcat/conf/

# Configure logging to forward tomcat logs to docker log collector (reduced to single layer)
RUN    ln -s /dev/stderr /usr/local/tomcat/logs/localhost.log \
    && ln -s /dev/stderr /usr/local/tomcat/logs/stacktrace.log

EXPOSE 8080
CMD ["bin/run.sh"]
