#!/bin/bash
INSTANCE=zdevl


echo "Removing old war and folder"
rm -rf IntegrationApi.war
rm -rf IntegrationApi

echo "Making new folder"
mkdir IntegrationApi

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/IntegrationApi.war .

echo "Extracting war"
cd IntegrationApi
unzip ../IntegrationApi.war
cd ..

