#!/bin/bash

APP_NAME=$1
VERSION=$2
INSTANCE=$3
DATE=$(date +%Y%m%d)

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf $APP_NAME.war
rm -rf $APP_NAME
rm -rf saml
rm -rf saml.zip

#Make new App Directory
echo "Create new app directory"
mkdir $APP_NAME
mkdir saml

echo "Downloading war from build.banner"
scp -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APP_NAME.war .
ssh -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd $APP_NAME
jar xvf ../$APP_NAME.war 
cd ..
cd saml
unzip ../saml.zip
cd ..

cp WEB-INF/classes/* $APP_NAME/WEB-INF/classes/

echo "$APP_NAME $VERSION is ready for configuration"

rm Dockerfile

echo "FROM usuit/banner:base-bannerselfservice-9.0.84-jdk8-corretto-cacerts" > Dockerfile

echo "LABEL version=$VERSION" >> Dockerfile

echo "USER root" >> Dockerfile
echo "ENV TIMEZONE=America/Denver" >> Dockerfile
echo 'ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/'$APP_NAME'/WEB-INF/classes/banner_configuration.groovy"' >> Dockerfile
echo 'RUN cp -f /usr/share/zoneinfo/America/Denver /etc/localtime' >> Dockerfile
echo 'RUN echo America/Denver> /etc/timezone' >> Dockerfile 
echo 'USER tomcat' >> Dockerfile

echo 'COPY --chown=tomcat:tomcat '$APP_NAME' /usr/local/tomcat/webapps/'$APP_NAME >> Dockerfile
echo 'COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/'$APP_NAME'/saml' >> Dockerfile
if ["$APP_NAME" == "FacultySelfService"]
then
    echo 'COPY --chown=tomcat:tomcat gradeEntry.json /usr/local/tomcat/webapps/'$APP_NAME'/gradeEntry.json' >> Dockerfile
fi
APP_NAME_LOWER=$(echo "$APP_NAME" | tr '[:upper:]' '[:lower:]')
docker build --platform linux/amd64 -t usuit/banner:$APP_NAME_LOWER-$VERSION-$INSTANCE-$DATE .
docker push usuit/banner:$APP_NAME_LOWER-$VERSION-$INSTANCE-$DATE

cd /home/rancher/k8s-config/bannerdev
source .envrc
kubectl set image deployment/$APP_NAME_LOWER $APP_NAME_LOWER=usuit/banner:$APP_NAME_LOWER-$VERSION-$INSTANCE-$DATE -n $INSTANCE
kubectl scale deployment $APP_NAME_LOWER --replicas=1 -n $INSTANCE
