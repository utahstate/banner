#!/bin/bash
INSTANCE=zdevl
echo "Removing old wars and folders"
rm -rf BannerAdminBPAPI
rm -rf BannerAdminBPAPI.war
rm -rf BannerAdminBPAPI_configs

echo "Making new folders"
mkdir BannerAdminBPAPI
mkdir BannerAdminBPAPI_configs

echo "Downloading files from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAdminBPAPI.war .
ssh root@build.banner.usu.edu "cd /u01/deploy/$INSTANCE/BannerAdminBPAPI/ && rm BannerAdminBPAPI_configs.zip && zip -r BannerAdminBPAPI_configs.zip ./*"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerAdminBPAPI/BannerAdminBPAPI_configs.zip .
cd BannerAdminBPAPI_configs
unzip ../BannerAdminBPAPI_configs.zip
sed -i -e "s|u01/deploy/$INSTANCE|usr/local/tomcat/webapps|g" config/config.xml
sed -i -e "s|u01/deploy/$INSTANCE|usr/local/tomcat/webapps|g" config/config.properties

cd .. 


echo "Extracting BannerAdmin Business Process API"
cd BannerAdminBPAPI
jar xvf ../BannerAdminBPAPI.war
cd ..

echo "Banner Admin Business Process API is ready for configuration"
