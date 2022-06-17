#!/bin/bash
INSTANCE=zpprd
echo "Removing old wars and folders"
rm -rf BannerAccessMgmt
rm -rf BannerAcessMgmt.ws
#rm -rf bannerHelp
rm -rf BannerAccessMgmt.war
rm -rf BannerAccessMgmt.ws.war
#rm -rf bannerHelp.war

echo "Making new folders"
mkdir BannerAccessMgmt BannerAccessMgmt.ws

echo "Downloading files from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerPages/BannerAccessMgmt.war .
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerPages/BannerAccessMgmt.ws.war .
#scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerPages/bannerHelp.war .

echo "Extracting BannerAccessMgmt"
cd BannerAccessMgmt
jar xvf ../BannerAccessMgmt.war
cd ..

echo "Extracting BannerAccessMgmt.ws"
cd BannerAccessMgmt.ws
jar xvf ../BannerAccessMgmt.ws.war
cd ..

