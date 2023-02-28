#!/bin/bash

INSTANCE=zpprd

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf BannerExtensibility.war
rm -rf BannerExtensibility
rm -rf saml.zip
rm -rf saml

#Make new App Directory
echo "Create new app directory"
mkdir BannerExtensibility
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerExtensibility.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd BannerExtensibility
jar xvf ../BannerExtensibility.war 
cd ..
cd saml
unzip ../saml.zip
cd ..

cp WEB-INF/classes/* BannerExtensibility/WEB-INF/classes/

echo "BannerExtensibility is ready for configuration"
