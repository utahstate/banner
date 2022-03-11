#!/bin/bash

INSTANCE=zdevl

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf brim.war
rm -rf brim

#Make new App Directory
echo "Create new app directory"
mkdir brim

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/brim.war .

echo "Extracting war"
cd brim
jar xvf ../brim.war 
cd ..

echo "Brim is ready for configuration"
