FROM usuit/banner-base:build-jdk17-latest AS build
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root.

RUN --mount=type=ssh <<EOF bash
		set -e
# Download files from build.banner and extract
		download-wars "${instance}" BannerAccessMgmt{,.ws} BannerAdmin.ws
# Not sure why we need these, but they're in the old prep.sh script, so we'll copy them here.
		cp BannerAdmin.ws/WEB-INF/lib/jackson-* BannerAccessMgmt.ws/WEB-INF/lib/
		rm -f Banner{AccessMgmt,Admin}.ws/WEB-INF/lib/reload4j-1.2.22.jar
# Grab SAML files like how the old script does
		scp "build.banner:/u01/saml/${instance}/${instance}-bam*" BannerAccessMgmt.ws/WEB-INF/classes/
EOF

FROM usuit/banner-base:9-jdk17-tomcat10-latest AS final

ENV APP_NAME="BannerAccessMgmt"
LABEL	edu.usu.banner.appName="${APP_NAME}"

ENV CATALINA_OPTS="-server -Xms\$XMS -Xmx\$XMX -XXMaxMetaspaceSize=1024m -Dlog4j.configuration=config.properties -Duser.timezone=\$TIMEZONE -Doracle.jdbc.autoCommitSpecCompliant=false"

COPY --chown=tomcat:tomcat --from=build /opt/build/BannerAccessMgmt /usr/local/tomcat/webapps/BannerAccessMgmt
COPY --chown=tomcat:tomcat --from=build /opt/build/BannerAccessMgmt.ws /usr/local/tomcat/webapps/BannerAccessMgmt.ws
# Load in config files
COPY --chown=tomcat:tomcat context.xml server.xml web.xml /usr/local/tomcat/conf/
