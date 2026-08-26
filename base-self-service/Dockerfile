FROM usuit/banner-base:9-jdk21-tomcat10

ENV APP_TYPE="self-service"
LABEL edu.usu.banner.appType="${APP_TYPE}"

COPY --chown=tomcat:tomcat \
		server.xml \
		context.xml \
		web.xml \
		/usr/local/tomcat/conf/

RUN mkdir /opt/banner/images
RUN mkdir /opt/banner/config
