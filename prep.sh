#!/bin/bash

INSTANCE=zpprd

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf StudentRegistrationSsb.war
rm -rf StudentRegistrationSsb
rm -rf saml.zip
rm -rf saml

#Make new App Directory
echo "Create new app directory"
mkdir StudentRegistrationSsb
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/StudentRegistrationSsb.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd StudentRegistrationSsb
jar xvf ../StudentRegistrationSsb.war 
cd ..
cd saml
unzip ../saml.zip
cd ..

echo "StudentRegistrationSsb is ready for configuration"
