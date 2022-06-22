#!/bin/bash
INSTANCE=zpprd
CLEANADDRESS=false
echo "Removing old wars and folders"
rm -rf BannerAdmin
rm -rf BannerAdmin.ws
#rm -rf bannerHelp
rm -rf BannerAdmin.war
rm -rf BannerAdmin.ws.war
#rm -rf bannerHelp.war
rm -rf saml.zip
rm -rf saml

echo "Making new folders"
mkdir BannerAdmin BannerAdmin.ws bannerHelp saml

echo "Downloading files from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAdmin.war .
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAdmin.ws.war .
#scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerPages/bannerHelp.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting BannerAdmin"
cd BannerAdmin
unzip ../BannerAdmin.war
cd ..

echo "Extracting BannerAdmin.ws"
cd BannerAdmin.ws
unzip ../BannerAdmin.ws.war
cd ..

#echo "Extracting bannerHelp"
#cd bannerHelp
#unzip ../bannerHelp.war
#cd ..
cd saml
unzip ../saml.zip
cd ..

if $CLEANADDRESS; then
echo "Updating BannerAdmin.ws config for CleanAddress"
CONFIGPROPFILEPATH="BannerAdmin.ws/WEB-INF/classes"
if ! cat $CONFIGPROPFILEPATH/config.properties | tr -d [[:space:]] | grep -iq "plugins=com.runnertech.ext"; then
cd BannerAdmin.ws/WEB-INF/classes
echo -e "##############################\n# Plug-in Definition\n##############################\n\nplugins = com.runnertech.ext\n\n$(cat config.properties)" > config.properties
cd ../../../
else
 echo "Plugin definition already in config.properties. Nothing to do."
fi
head -10 $CONFIGPROPFILEPATH/config.properties
fi
