#!/bin/bash

INSTANCE=zpprd
APP=CommunicationManagement

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf $APP.war
rm -rf $APP
rm -rf saml.zip
rm -rf saml

#Make new App Directory
echo "Create new app directory"
mkdir $APP
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APP.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd $APP
jar xvf ../$APP.war
cd ..
cd saml
jar xvf ../saml.zip
cd ..

cp WEB-INF/classes/* CommunicationManagement/WEB-INF/classes/

echo "$APP is ready for configuration"
