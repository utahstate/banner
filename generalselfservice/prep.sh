#!/bin/bash

INSTANCE=zdevl
CLEANADDRESS=true
APP=BannerGeneralSsb
VERSION=9.12.1.1
ZIP_PASSWORD=transcript
WARFILE=$(pwd)/BannerGeneralSsb.war
CURRENT_FOLDER=$(pwd)

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf $APP.war
rm -rf *.war*
rm -rf $APP
rm -rf saml.zip
rm -rf saml

#Make new App Directory
echo "Create new app directory"
mkdir $APP
mkdir saml

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APP.war .
ssh root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp root@build.banner.usu.edu:/u01/saml/saml.zip .

if $CLEANADDRESS; then
echo "Downloading clean address plugin for general self service"
curl -o clnbannerssb_$VERSION.zip https://files.runneredq.com/integrations/RunnerEDQ-Banner9SSB/clnbannerssb_$VERSION.zip
rm -rf clnbannerssb_$VERSION
unzip -P $ZIP_PASSWORD clnbannerssb_$VERSION.zip -d ./clnbannerssb_$VERSION
cd clnbannerssb_$VERSION/clnaddr_banner_ssb
cp assets/modules/personalInformationApp-mf.js assets/modules/personalInformationApp-mf-$VERSION.js
cp assets/modules/pi-application-mf.js assets/modules/pi-application-mf-$VERSION.js
cp assets/personalInformationApp/piEmergencyContact/piEditEmergencyContact-controller.js assets/personalInformationApp/piEmergencyContact/piEditEmergencyContact-controller-$VERSION.js
cp assets/personalInformationApp/piEmergencyContact/cleanAddressEmergency-controller.js assets/personalInformationApp/piEmergencyContact/cleanAddressEmergency-controller-$VERSION.js
cp assets/personalInformationApp/piEmergencyContact/piEditEmergencyContact.html assets/personalInformationApp/piEmergencyContact/piEditEmergencyContact-$VERSION.html
cp assets/personalInformationApp/common/services/cleanAddress-service.js assets/personalInformationApp/common/services/cleanAddress-service-$VERSION.js
cp assets/personalInformationApp/piAddress/piEditAddress-controller.js assets/personalInformationApp/piAddress/piEditAddress-controller-$VERSION.js
cp assets/personalInformationApp/piAddress/cleanAddressMain-controller.js assets/personalInformationApp/piAddress/cleanAddressMain-controller-$VERSION.js
cp assets/personalInformationApp/piAddress/piEditAddress.html assets/personalInformationApp/piAddress/piEditAddress-$VERSION.html

modifyManifest(){

  # Files included in integration to be added to manifest.properties
  echo "# CLEAN_Address STARTS HERE" >> assets/manifest.properties
  sed -i -e '/'pi-application-mf.js'/d' assets/manifest.properties  
  echo "modules/pi-application-mf.js=modules/pi-application-mf-$VERSION.js" >> assets/manifest.properties
  echo "modules/personalInformationApp-mf.js=modules/personalInformationApp-mf-$VERSION.js" >> assets/manifest.properties
  echo "personalInformationApp/piEmergencyContact/piEditEmergencyContact-controller.js=personalInformationApp/piEmergencyContact/piEditEmergencyContact-controller-$VERSION.js" >> assets/manifest.properties
  echo "personalInformationApp/piEmergencyContact/cleanAddressEmergency-controller.js=personalInformationApp/piEmergencyContact/cleanAddressEmergency-controller-$VERSION.js" >> assets/manifest.properties
  echo "personalInformationApp/piEmergencyContact/piEditEmergencyContact.html=personalInformationApp/piEmergencyContact/piEditEmergencyContact-$VERSION.html" >> assets/manifest.properties
  echo "personalInformationApp/common/services/cleanAddress-service.js=personalInformationApp/common/services/cleanAddress-service-$VERSION.js" >> assets/manifest.properties
  echo "personalInformationApp/piAddress/piEditAddress-controller.js=personalInformationApp/piAddress/piEditAddress-controller-$VERSION.js" >> assets/manifest.properties
  echo "personalInformationApp/piAddress/cleanAddressMain-controller.js=personalInformationApp/piAddress/cleanAddressMain-controller-$VERSION.js" >> assets/manifest.properties
  echo "personalInformationApp/piAddress/piEditAddress.html=personalInformationApp/piAddress/piEditAddress-$VERSION.html" >> assets/manifest.properties
  echo "# CLEAN_Address ENDS HERE" >> assets/manifest.properties

}


echo "Backing up original $WARFILE..."
cp $WARFILE $WARFILE-$TIMESTAMP
echo "Extracting and updating assets/manifest.properties..."
jar xvf $WARFILE assets/manifest.properties
echo "Updating assets/manifest.properties"
modifyManifest
echo "Updating war file with integration files..."
jar -uvf $WARFILE assets/
jar -uvf $WARFILE WEB-INF/
echo
echo "Done!"
fi

echo "Extracting war"
cd $CURRENT_FOLDER/$APP
jar xvf ../$APP.war
cd ..
cd saml
unzip ../saml.zip
cd ..

cp assets/* $APP/assets/

cp WEB-INF/classes/* $APP/WEB-INF/classes/

echo "$APP is ready for configuration"

