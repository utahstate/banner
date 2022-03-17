#!/bin/bash
INSTANCE=zdevl
echo "Removing old wars and folders"
rm -rf BannerAdmin
rm -rf BannerAdmin.ws
#rm -rf bannerHelp
rm -rf BannerAdmin.war
rm -rf BannerAdmin.ws.war
#rm -rf bannerHelp.war

echo "Making new folders"
mkdir BannerAdminBPAPI

echo "Downloading files from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAdminBPAPI.war .

echo "Extracting BannerAdmin Business Process API"
cd BannerAdminBPAPI
unzip ../BannerAdminBPAPI.war
cd ..

echo "Banner Admin Business Process API is ready for configuration"
