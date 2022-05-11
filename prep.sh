#!/bin/bash

INSTANCE=zdevl

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf StudentSelfService.war
rm -rf StudentSelfService
rm -rf saml
rm -rf saml.zip

#Make new App Directory
echo "Create new app directory"
mkdir StudentSelfService
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/StudentSelfService.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd StudentSelfService
jar xvf ../StudentSelfService.war 
cd ..
cd saml
unzip ../saml.zip
cd ..

echo "StudentSelfService is ready for configuration"
