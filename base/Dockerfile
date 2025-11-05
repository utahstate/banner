FROM tomcat:9-jdk17-corretto

LABEL edu.usu.banner.versionMajor="9"

ENV TIMEZONE="America/Denver" \
    XMS=2g XMX=4g \
    MAXMETASPACE=512m \
    LOGGING_DIR=/usr/local/tomcat/logs \
    CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false -Dbanner.logging.dir=\$LOGGING_DIR"

# Run the updates with /var/cache as a cache mount, which will preserve it for later stages without including it in any layers in the final image.
RUN --mount=type=cache,target=/var/cache,sharing=locked \
    yum update -y \
    && amazon-linux-extras install epel \
    && yum install -y xmlstarlet shadow-utils \
    && rm -Rf /usr/local/tomcat/webapps/* \
    && ln -sf /usr/share/zoneinfo/$TIMEZONE /etc/localtime \
    && groupadd -r tomcat && useradd -r -g tomcat tomcat

# REMOVED certs may no longer be needed
# # cert management must be done as root
# COPY USERTrust_RSA_Certification_Authority.cer InCommon_RSA_Server_CA_2.cer /etc/certs/
# RUN    keytool -import -alias usertrust -file /etc/certs/USERTrust_RSA_Certification_Authority.cer -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt \
#     && keytool -import -alias incommon  -file /etc/certs/InCommon_RSA_Server_CA_2.cer              -keystore /usr/lib/jvm/java-17-amazon-corretto/lib/security/cacerts -storepass changeit -noprompt

# Download ojdbc11-full.tar.gz
ADD --chmod=644 https://download.oracle.com/otn-pub/otn_software/jdbc/239/ojdbc11-full.tar.gz /usr/local/tomcat

# Unpack ojdbc11-full.tar.gz. Use a tmpfs on /tmp so the unpacked files are automatically cleaned up, and pull in the cache mount from earlier so we can install packages
RUN --mount=type=tmpfs,target=/tmp --mount=type=cache,target=/var/cache/,sharing=locked <<EOF
    set -e
    cd /tmp
# Install tar and gzip packages here so we can remove them and not include them in a layer -- we don't need them at runtime.
    yum install -y tar gzip
		tar xzf /usr/local/tomcat/ojdbc11-full.tar.gz
    mv ojdbc11.jar xdb.jar ucp11.jar /usr/local/tomcat/lib/
    rm /usr/local/tomcat/ojdbc11-full.tar.gz
    yum remove -y tar gzip
EOF

# Create directories that some derived images use and chown them to tomcat:tomcat to remove the need to temporarily set USER to root to create these dirs
RUN <<EOF
    mkdir -p /opt/banner/ /u01
    chown -R tomcat: /opt/banner /u01 /usr/local/tomcat
EOF

# Switch to tomcat user
USER tomcat

# Redirect logs to stderr to dump them into container runtime log manager
COPY logging.properties /usr/local/tomcat/conf/logging.properties
RUN    ln -s /dev/stderr /usr/local/tomcat/logs/localhost.log \
    && ln -s /dev/stderr /usr/local/tomcat/logs/stacktrace.log

# Most services run on port 8080
EXPOSE 8080

# Expects a config volume at /usr/local/tomcat/conf/catalina.properties.d
# The searched directory can be configured with the $PROPERTY_DROPINS environment variable, and should
# generally be configured to a subdirectory containing the configuration (so that common config can be
# symlinked and still function in containers). This replaces the environment-based configuration from the old
# images.
VOLUME /usr/local/tomcat/conf/catalina.properties.d

ENTRYPOINT ["bin/run.sh"]
