#!/bin/bash

APP_NAME=$1
VERSION=$2
INSTANCE=$3
DATE=$(date +%Y%m%d-%S)
ZIP_PASSWORD=transcript
WARFILE=$(pwd)/$APP_NAME.war
CURRENT_FOLDER=$(pwd)
APP_NAME_LOWER=$(echo "$APP_NAME" | tr '[:upper:]' '[:lower:]')

echo "Script arguments are: $1 $2 $3"
echo "Prep.sh APP_NAME is $APP_NAME"
echo "Prep.sh VERSION is $VERSION"
echo "Prep.sh INSTANCE is $INSTANCE"

CLEANADDRESS=false
if [[ "$INSTANCE" == "zdevl" ]] || [[ "$INSTANCE" == "zprod" ]]; then
        if [[ "$APP_NAME" == "BannerGeneralSsb" ]] || [[ "$APP_NAME" == "BannerAdmin" ]]; then
                CLEANADDRESS=true
        fi
fi

#Remove old war and app folder
echo "remove old war and app folder"
rm -rf $APP_NAME.war
rm -rf $APP_NAME
rm -rf $APP_NAME.ws.war
rm -rf $APP_NAME.ws
rm -rf BannerAdminBPAPI_configs
rm -rf saml
rm -rf saml.zip

#Make new App Directory
echo "Create new app directory"
mkdir $APP_NAME
if [[ "$APP_NAME" == "BannerAdmin" ]] || [[ "$APP_NAME" == "BannerAccessMgmt" ]]; then
	mkdir $APP_NAME.ws
fi
mkdir saml

echo "Downloading war from build.banner"
scp -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APP_NAME.war .
if [[ "$APP_NAME" == "BannerAdmin" ]] || [[ "$APP_NAME" == "BannerAccessMgmt" ]]; then
	scp -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/$APP_NAME.ws.war .
fi
if [[ "$APP_NAME" == "BannerAdminBPAPI" ]]; then
	ssh -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu "cd /u01/deploy/$INSTANCE/BannerAdminBPAPI/ && rm BannerAdminBPAPI_configs.zip && zip -r BannerAdminBPAPI_configs.zip ./*"
        scp -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu:/u01/deploy/$INSTANCE/BannerAdminBPAPI/BannerAdminBPAPI_configs.zip .
	mkdir BannerAdminBPAPI_configs
        cd BannerAdminBPAPI_configs
        unzip ../BannerAdminBPAPI_configs.zip
	cp /home/rancher/github/banner/businessprocessapi/applicationContext.xml $CURRENT_FOLDER/applicationContext.xml
	cp /home/rancher/github/banner/businessprocessapi/run.sh $CURRENT_FOLDER/run.sh
        sed -i -e "s|u01/deploy/$INSTANCE|usr/local/tomcat/webapps|g" config/config.xml
        sed -i -e "s|u01/deploy/$INSTANCE|usr/local/tomcat/webapps|g" config/config.properties
        cd ..
fi
ssh -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu "cd /u01/saml && rm saml.zip && zip -r saml.zip ./*"
scp -i /home/rancher/.ssh/id_ed25519 root@build.banner.usu.edu:/u01/saml/saml.zip .

if $CLEANADDRESS && [[ "$APP_NAME" == "BannerGeneralSsb" ]]; then
#Uncomment these lines after 20241212
#cd /home/rancher/github/banner/base-bcm
#docker build --pull --platform linux/amd64 -t usuit/banner:base-bcm-9-jdk17-corretto-cacerts .
#docker push usuit/banner:base-bcm-9-jdk17-corretto-cacerts
#cd $CURRENT_FOLDER
echo "Downloading clean address plugin for general self service"
curl -o clnbannerssb_$VERSION.zip https://files.runneredq.com/integrations/RunnerEDQ-Banner9SSB/clnbannerssb_$VERSION.zip
rm -rf clnbannerssb_$VERSION
7z x -p$ZIP_PASSWORD clnbannerssb_$VERSION.zip -oclnbannerssb_$VERSION
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
cd $CURRENT_FOLDER/$APP_NAME
jar xvf ../$APP_NAME.war 
cd ..

if [[ "$APP_NAME" == "BannerAdmin" ]] || [[ "$APP_NAME" == "BannerAccessMgmt" ]]; then
	cd $CURRENT_FOLDER/$APP_NAME.ws
	jar xvf ../$APP_NAME.ws.war
	cd ..
	echo "Running sameSiteCookies and applicationContext.xml.saml..."
	sed -i -e "s|\<\!-- Insert Environment.*|<CookieProcessor sameSiteCookies=\"none\" />|g" $APP_NAME.ws/META-INF/context.xml
	cp /home/rancher/github/banner/$APP_NAME_LOWER/applicationContext.xml.saml $CURRENT_FOLDER/applicationContext.xml.saml
	cp /home/rancher/github/banner/$APP_NAME_LOWER/run.sh $CURRENT_FOLDER/run.sh
fi

if [[ $APP_NAME == applicationNavigator ]]; then
	cp /home/rancher/github/banner/$APP_NAME_LOWER/context.xml $CURRENT_FOLDER/context.xml
fi

if [[ "$APP_NAME" == "BannerAdmin" ]] && $CLEANADDRESS; then
echo "Updating BannerAdmin.ws config for CleanAddress"
CONFIGPROPFILEPATH="BannerAdmin.ws/WEB-INF/classes"
if ! cat $CONFIGPROPFILEPATH/config.properties | tr -d '[:space:]' | grep -iq "plugins=com.runnertech.ext"; then
cd BannerAdmin.ws/WEB-INF/classes
echo -e "##############################\n# Plug-in Definition\n##############################\n\nplugins = com.runnertech.ext\n\n$(cat config.properties)" > config.properties
cd ../../../
else
 echo "Plugin definition already in config.properties. Nothing to do."
fi
head -10 $CONFIGPROPFILEPATH/config.properties
fi

cd saml
unzip ../saml.zip
cd ..
cp /home/rancher/github/banner/$APP_NAME_LOWER/WEB-INF/classes/* $APP_NAME/WEB-INF/classes/

echo "$APP_NAME $VERSION is ready for configuration"

rm Dockerfile

if [[ $APP_NAME == *SelfService ]] || [[ $APP_NAME == brim ]] || [[ $APP_NAME == applicationNavigator ]] || [[ $APP_NAME == DocumentManagementApi ]] || [[ $APP_NAME == eTranscriptAPI ]] || [[ $APP_NAME == StudentApi ]] || [[ $APP_NAME == IntegrationApi ]] || [[ $APP_NAME == StudentRegistrationSsb ]] || [[ $APP_NAME == BannerExtensibility ]]; then
        if [[ $INSTANCE == zprod ]] || [[ $INSTANCE == wprod ]]; then
		echo "FROM usuit/banner:base-bannerselfservice-9.0.93-jdk8-corretto-cacerts" > Dockerfile
	else
        	cd /home/rancher/github/banner/banner9-selfservice
        	docker build --pull --platform linux/amd64 -t usuit/banner:base-bannerselfservice-9-jdk17-corretto .
        	docker push usuit/banner:base-bannerselfservice-9-jdk17-corretto
        	cd $CURRENT_FOLDER
		echo "FROM usuit/banner:base-bannerselfservice-9-jdk17-corretto" > Dockerfile
	fi
fi

if [[ $APP_NAME == BannerEventPublisher ]]; then
	if [[ $INSTANCE == zprod ]] || [[ $INSTANCE == wprod ]]; then
		echo "FROM usuit/banner:base-bep-9.0.93-jdk8-corretto-cacerts" > Dockerfile
	else
		cd /home/rancher/github/banner/base-bep
		docker build --pull --platform linux/amd64 -t usuit/banner:base-bep-9-jdk17-corretto .
		docker push usuit/banner:base-bep-9-jdk17-corretto
		cd $CURRENT_FOLDER
		echo "FROM usuit/banner:base-bep-9-jdk17-corretto" > Dockerfile
	fi
fi

if [[ $APP_NAME == BannerGeneralSsb ]] || [[ $APP_NAME == CommunicationManagement ]]; then
	if [[ $INSTANCE == zprod ]] || [[ $INSTANCE == wprod ]]; then
		echo "FROM usuit/banner:base-bcm-9.0.93-jdk8-corretto-cacerts" > Dockerfile
	else
		cd /home/rancher/github/banner/base-bcm
		docker build --pull --platform linux/amd64 -t usuit/banner:base-bcm-9-jdk17-corretto .
		docker push usuit/banner:base-bcm-9-jdk17-corretto
		cd $CURRENT_FOLDER
		echo "FROM usuit/banner:base-bcm-9-jdk17-corretto" > Dockerfile
	fi
fi

if [[ $APP_NAME == BannerAdmin ]] || [[ $APP_NAME == BannerAdminBPAPI ]] || [[ $APP_NAME == BannerAccessMgmt ]]; then
        if [[ $INSTANCE == zprod ]] || [[ $INSTANCE == wprod ]]; then
		echo "FROM usuit/banner:base-banneradmin-9.0.93-jdk8-corretto-cacerts" > Dockerfile
	else
        	cd /home/rancher/github/banner/banner9-admin
        	docker build --pull --platform linux/amd64 -t usuit/banner:base-banneradmin-10-jdk17 .
        	docker push usuit/banner:base-banneradmin-10-jdk17
        	cd $CURRENT_FOLDER
		echo "FROM usuit/banner:base-banneradmin-10-jdk17" > Dockerfile
	fi
fi

echo "LABEL version=$VERSION" >> Dockerfile

echo "USER root" >> Dockerfile
echo "ENV TIMEZONE=America/Denver" >> Dockerfile
echo 'ENV JAVA_OPTS="-DBANNER_APP_CONFIG=/usr/local/tomcat/webapps/'$APP_NAME'/WEB-INF/classes/banner_configuration.groovy"' >> Dockerfile
echo 'RUN cp -f /usr/share/zoneinfo/America/Denver /etc/localtime' >> Dockerfile
echo 'RUN echo America/Denver> /etc/timezone' >> Dockerfile 
echo 'RUN mkdir /u01' >> Dockerfile
if [[ $APP_NAME == DocumentManagementApi ]] || [[ $APP_NAME == FinanceSelfService ]]; then
        echo 'RUN mkdir -p /opt/banner/xtender && chown tomcat:tomcat /opt/banner/xtender' >> Dockerfile
fi
echo 'COPY saml /u01/saml' >> Dockerfile
if [[ $APP_NAME == BannerAdminBPAPI ]] || [[ $APP_NAME == BannerAccessMgmt ]]; then
        echo 'COPY run.sh /usr/local/tomcat/bin' >> Dockerfile
        echo 'RUN chown -R tomcat:tomcat /usr/local/tomcat && chmod +x /usr/local/tomcat/bin/run.sh' >> Dockerfile
fi
if [[ $APP_NAME == StudentSelfService ]]; then
	echo 'COPY server.xml.sss /usr/local/tomcat/conf/server.xml' >> Dockerfile
	echo 'RUN chown -R tomcat:tomcat /usr/local/tomcat' >> Dockerfile
fi
echo 'USER tomcat' >> Dockerfile
echo 'COPY --chown=tomcat:tomcat '$APP_NAME' /usr/local/tomcat/webapps/'$APP_NAME >> Dockerfile
if [[ $APP_NAME == BannerAdmin ]] || [[ $APP_NAME == BannerAccessMgmt ]]; then
	echo 'COPY --chown=tomcat:tomcat '$APP_NAME'.ws /usr/local/tomcat/webapps/'$APP_NAME'.ws' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat applicationContext.xml.saml /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/applicationContext.xml.saml' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat saml/aws/aws-adminpages* /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/classes/' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat saml/zpprd/zpprd-adminpages* /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/classes/' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat saml/zdevl/zdevl-adminpages* /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/classes/' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat saml/zprod/zprod-adminpages* /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/classes/' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat saml/wpprd/wpprd-adminpages* /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/classes/' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat saml/wprod/wprod-adminpages* /usr/local/tomcat/webapps/'$APP_NAME'.ws/WEB-INF/classes/' >> Dockerfile
fi
if [[ $APP_NAME == brim ]]; then
	cp /home/rancher/github/banner/$APP_NAME_LOWER/javax.jms_1.1.1.jar $CURRENT_FOLDER/javax.jms_1.1.1.jar
	echo 'COPY --chown=tomcat:tomcat javax.jms_1.1.1.jar /usr/local/tomcat/lib/javax.jms_1.1.1.jar' >> Dockerfile
fi
if [[ $APP_NAME == applicationNavigator ]]; then
	#Uncomment these lines after 20241212
        #cd /home/rancher/github/banner/banner9-selfservice
        #docker build --pull --platform linux/amd64 -t usuit/banner:base-bannerselfservice-9-jdk17-corretto .
        #docker push usuit/banner:base-bannerselfservice-9-jdk17-corretto
        #cd $CURRENT_FOLDER
	echo 'COPY --chown=tomcat:tomcat context.xml /usr/local/tomcat/webapps/applicationNavigator/META-INF/context.xml' >> Dockerfile
fi
echo 'COPY --chown=tomcat:tomcat saml /usr/local/tomcat/webapps/'$APP_NAME'/saml' >> Dockerfile
if [[ "$APP_NAME" == "FacultySelfService" ]]
then
	cp /home/rancher/github/banner/$APP_NAME_LOWER/gradeEntry.json $CURRENT_FOLDER/gradeEntry.json
        echo 'COPY --chown=tomcat:tomcat gradeEntry.json /usr/local/tomcat/webapps/'$APP_NAME'/gradeEntry.json' >> Dockerfile
fi
if [[ $APP_NAME == BannerAdminBPAPI ]]; then
	echo 'COPY --chown=tomcat:tomcat BannerAdminBPAPI_configs /usr/local/tomcat/webapps/BannerAdminBPAPI' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat BannerAdminBPAPI_configs/config /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/config' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat BannerAdminBPAPI_configs/config/* /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/' >> Dockerfile
        echo 'COPY --chown=tomcat:tomcat applicationContext.xml /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/' >> Dockerfile
fi
docker build --platform linux/amd64 -t usuit/banner:$APP_NAME_LOWER-$VERSION-$INSTANCE-$DATE .
docker push usuit/banner:$APP_NAME_LOWER-$VERSION-$INSTANCE-$DATE

rm run.sh
rm context.xml
rm applicationContext.xml.saml
rm -rf *.war

if [[ $INSTANCE == zprod ]] || [[ $INSTANCE == zldtst ]] || [[ $INSTANCE == wprod ]]; then
	cd /home/rancher/k8s-config/banner
fi
if [[ $INSTANCE == zdevl ]] || [[ $INSTANCE == zpprd ]] || [[ $INSTANCE == wpprd ]]; then
        cd /home/rancher/k8s-config/bannerdev
fi
	source .envrc
	kubectl set image deployment/$APP_NAME_LOWER $APP_NAME_LOWER=usuit/banner:$APP_NAME_LOWER-$VERSION-$INSTANCE-$DATE -n $INSTANCE
if [[ $INSTANCE == zprod ]]; then
	/home/rancher/k8s-config/banner/zprod_scale_$APP_NAME_LOWER.sh
else
	kubectl scale deployment $APP_NAME_LOWER --replicas=1 -n $INSTANCE
fi

docker system prune -af
