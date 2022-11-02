#!/bin/bash
INSTANCE=zdevl

echo "Removing old war and folder"
rm -rf FacultySelfService.war
rm -rf FacultySelfService
rm -rf saml.zip
rm -rf saml

echo "Making new folder"
mkdir FacultySelfService
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/FacultySelfService.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd FacultySelfService
jar xvf ../FacultySelfService.war
cd ..

cd saml
unzip ../saml.zip
cd ..

cp WEB-INF/classes/* FacultySelfService/WEB-INF/classes/
