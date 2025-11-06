FROM usuit/banner-base:build-jdk17-latest AS build
# Base image for build stages that has SSH, Git, and similar utilities already installed. Runs as root in the container.

RUN --mount=type=ssh <<EOF bash
		set -e
		echo "Downloading applicationNavigator WAR for ${instance} from build.banner"
		cat ~/.ssh/config
		scp "build.banner:/u01/deploy/${instance}/self-service/applicationNavigator.war" .
		mkdir -p applicationNavigator
		cd applicationNavigator
		jar xvf ../applicationNavigator.war
EOF

FROM usuit/banner-base:9-jdk17-tomcat9-latest AS final

ENV APP_NAME="applicationNavigator"
LABEL edu.usu.banner.appName="${APP_NAME}"

ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/applicationNavigator/WEB-INF/classes/banner_configuration.groovy"

COPY --chown=tomcat:tomcat --from=build /opt/build/applicationNavigator /usr/local/tomcat/webapps/applicationNavigator
COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/applicationNavigator/META-INF/context.xml

# Apply patches
COPY --chown=tomcat:tomcat WEB-INF/classes/* /usr/local/tomcat/webapps/${APP_NAME}/WEB-INF/classes/
