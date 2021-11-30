#!/bin/bash
INSTANCE=zdevl

echo "Removing old war and folder"
rm -rf StudentApi.war
rm -rf StudentApi

echo "Making new folder"
mkdir StudentApi

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/StudentApi.war .

echo "Extracting war"
cd StudentApi
jar xvf ../StudentApi.war
cd ..
