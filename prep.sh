#!/bin/bash
INSTANCE=zpprd
echo "Removing old wars and folders"
rm -rf BannerAccessMgmt
rm -rf BannerAccessMgmt.ws
rm -rf BannerAdmin.ws
#rm -rf bannerHelp
rm -rf BannerAccessMgmt.war
rm -rf BannerAccessMgmt.ws.war
rm -rf BannerAdmin.ws.war
#rm -rf bannerHelp.war
rm -rf saml.zip
rm -rf saml

echo "Making new folders"
mkdir BannerAccessMgmt BannerAccessMgmt.ws BannerAdmin.ws
mkdir saml

echo "Downloading files from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAccessMgmt.war .
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAccessMgmt.ws.war .
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/BannerAdmin.ws.war .
#scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerPages/bannerHelp.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting BannerAccessMgmt"
cd BannerAccessMgmt
jar xvf ../BannerAccessMgmt.war
cd ..

echo "Extracting BannerAccessMgmt.ws"
cd BannerAccessMgmt.ws
jar xvf ../BannerAccessMgmt.ws.war
cd ..

echo "Extracting BannerAdmin.ws"
cd BannerAdmin.ws
jar xvf ../BannerAdmin.ws.war
cd ..

cp BannerAdmin.ws/WEB-INF/lib/jackson-* BannerAccessMgmt.ws/WEB-INF/lib/

cd saml
unzip ../saml.zip
cd ..

cp saml/zpprd/zpprd-bam* BannerAccessMgmt.ws/WEB-INF/classes/
cp saml/zdevl/zdevl-bam* BannerAccessMgmt.ws/WEB-INF/classes/
cp saml/zprod/zprod-bam* BannerAccessMgmt.ws/WEB-INF/classes/
