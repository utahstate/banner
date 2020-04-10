#!/bin/bash

INSTANCE=zpprd

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf StudentSelfService.war
rm -rf StudentSelfService

#Make new App Directory
echo "Create new app directory"
mkdir StudentSelfService

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/StudentSelfService.war .

echo "Extracting war"
cd StudentSelfService
jar xvf ../StudentSelfService.war 
cd ..

echo "StudentSelfService is ready for configuration"