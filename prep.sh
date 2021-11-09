#!/bin/bash

INSTANCE=zpprd

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf BannerExtensibility.war
rm -rf BannerExtensibility

#Make new App Directory
echo "Create new app directory"
mkdir BannerExtensibility

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerExtensibility.war .

echo "Extracting war"
cd BannerExtensibility
unzip ../BannerExtensibility.war 
cd ..

echo "BannerExtensibility is ready for configuration"
