FROM usuit/banner-base:build-jdk17-latest AS build
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root.
ARG version instance

RUN --mount=type=ssh <<EOF bash
		set -e
		echo "Downloading applicationNavigator WAR for ${instance} from build.banner"
		scp root@build.banner.usu.edu:/u01/deploy/${instance}/self-service/applicationNavigator.war .
		mkdir -p applicationNavigator
		cd applicationNavigator
		jar xvf ../applicationNavigator.war
		cd ..
		rm -f applicationNavigator.war
EOF

FROM usuit/banner-base:9-jdk17-latest AS final

ARG version instance
LABEL edu.usu.banner.version=${version}
LABEL edu.usu.banner.appName="applicationNavigator"
LABEL edu.usu.banner.instance=${instance}

ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/applicationNavigator/WEB-INF/classes/banner_configuration.groovy"

COPY --chown=tomcat:tomcat --from=build /opt/build/applicationNavigator /usr/local/tomcat/webapps/applicationNavigator
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/applicationNavigator/META-INF/context.xml

# Declare a volume for SAML files
VOLUME /usr/local/tomcat/webapps/applicationNavigator/saml
