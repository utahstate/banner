#!/bin/bash

INSTANCE=zdevl
APPNAME=BannerEventPublisher

#Remove old build
echo "Removing old war and app folder"
rm -rf $APPNAME
rm -rf $APPNAME.war

#Make App Directory
echo "Creating new app folder"
mkdir $APPNAME

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APPNAME.war .

echo "Extracting war"
cd $APPNAME
#unzip ../$APPNAME.war 
jar xvf ../$APPNAME.war
cd ..

echo "$APPNAME is ready for configuration"