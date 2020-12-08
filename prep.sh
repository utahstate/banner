#!/bin/bash

INSTANCE=zdevl

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf StudentRegistrationSsb.war
rm -rf StudentRegistrationSsb

#Make new App Directory
echo "Create new app directory"
mkdir StudentRegistrationSsb

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/StudentRegistrationSsb.war .

echo "Extracting war"
cd StudentRegistrationSsb
jar xvf ../StudentRegistrationSsb.war 
cd ..

echo "StudentRegistrationSsb is ready for configuration"
