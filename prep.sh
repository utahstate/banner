#!/bin/bash

INSTANCE=zdevl
APPNAME=EmployeeSelfService
#Remove old war and app folder
echo "Remove old war and app folder"
rm -rf $APPNAME.war
rm -rf $APPNAME

#Make new app folder
echo "Create new app directory"
mkdir $APPNAME

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APPNAME.war .

echo "Extracting war"
cd $APPNAME
jar xvf ../$APPNAME.war
cd ..

echo "$APPNAME is ready for configuration"