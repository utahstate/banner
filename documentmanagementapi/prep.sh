#!/bin/bash

INSTANCE=zdevl

#Remove old build
echo "Removing old war and app folder"
rm -rf DocumentManagementApi
rm -rf DocumentManagementApi.war

#Make App Directory
echo "Creating new app folder"
mkdir DocumentManagementApi

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/DocumentManagementApi.war .

echo "Extracting war"
cd DocumentManagementApi
jar -xvf ../DocumentManagementApi.war 
cd ..

cp WEB-INF/classes/* DocumentManagementApi/WEB-INF/classes/

echo "DocumentManagementApi is ready for configuration"
