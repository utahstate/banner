ARG tomcat_version=10.1 jdk=21
FROM tomcat:${tomcat_version}-jdk${jdk}

LABEL edu.usu.banner.versionMajor="9"

ENV TIMEZONE="America/Denver" \
    XMS=2g XMX=4g \
    MAXMETASPACE=512m \
    LOGGING_DIR=/usr/local/tomcat/logs \
    CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XX:MaxMetaspaceSize=\$MAXMETASPACE -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false -Dbanner.logging.dir=\$LOGGING_DIR"

# Download ojdbc11-full.tar.gz
ADD --chmod=644 https://download.oracle.com/otn-pub/otn_software/jdbc/239/ojdbc11-full.tar.gz /usr/local/tomcat

# Run the updates with /var/cache as a cache mount, which will preserve it for later stages without including it in any layers in the final image.
RUN --mount=type=cache,target=/var/cache,sharing=locked <<EOF
    set -e
    apt-get update
    apt-get install -y xmlstarlet
    rm -Rf /usr/local/tomcat/webapps/*
    ln -sf /usr/share/zoneinfo/$TIMEZONE /etc/localtime
    groupadd -r tomcat && useradd -r -g tomcat tomcat
EOF

# Unpack ojdbc11-full.tar.gz. Use a tmpfs on /tmp so the unpacked files are automatically cleaned up, and pull in the cache mount from earlier so we can install packages
RUN --mount=type=tmpfs,target=/tmp --mount=type=cache,target=/var/cache/,sharing=locked <<EOF
    set -e
    cd /tmp
# Install tar and gzip packages here so we can remove them and not include them in a layer -- we don't need them at runtime.
		tar --no-same-owner -xaf /usr/local/tomcat/ojdbc11-full.tar.gz
    mv ojdbc11.jar xdb.jar ucp11.jar /usr/local/tomcat/lib/
    rm /usr/local/tomcat/ojdbc11-full.tar.gz
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

# Copy run.sh
COPY --chmod=755 run.sh /usr/local/tomcat/bin/run.sh

# Most services run on port 8080
EXPOSE 8080

# Expects a config volume at /usr/local/tomcat/conf/catalina.properties.d
VOLUME /usr/local/tomcat/conf/catalina.properties.d

# SAML config volume
VOLUME /u01/saml

ONBUILD ARG version instance
ONBUILD LABEL edu.usu.banner.version=${version} \
		edu.usu.banner.instance=${instance}
ONBUILD ENV INSTANCE=${instance}


ENTRYPOINT ["bin/run.sh"]
CMD ["catalina.sh", "run"]
