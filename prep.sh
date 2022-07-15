#!/bin/bash

INSTANCE=zdevl
APPNAME=EmployeeSelfService
#Remove old war and app folder
echo "Remove old war and app folder"
rm -rf $APPNAME.war
rm -rf $APPNAME
rm -rf saml.zip
rm -rf saml


#Make new app folder
echo "Create new app directory"
mkdir $APPNAME
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APPNAME.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

echo "Extracting war"
cd $APPNAME
jar xvf ../$APPNAME.war
cd ..
cd saml
unzip ../saml.zip
cd ..

cp WEB-INF/classes/* $APPNAME/WEB-INF/classes/

echo "$APPNAME is ready for configuration"
