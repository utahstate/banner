
#!/bin/bash

INSTANCE=zpprd
APP=BannerGeneralSsb

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf $APP.war
rm -rf $APP

#Make new App Directory
echo "Create new app directory"
mkdir $APP

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APP.war .

echo "Extracting war"
cd $APP
jar xvf ../$APP.war
cd ..

echo "$APP is ready for configuration"