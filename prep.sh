#!/bin/bash --debug
function escape_slashes {
    sed 's/\//\\\//g' 
}

function change_line {
    local OLD_LINE_PATTERN=$1; shift
    local NEW_LINE=$1; shift
    local FILE=$1

    local NEW=$(echo "${NEW_LINE}" | escape_slashes)
    # FIX: No space after the option i.
    sed -i.bak '/'"${OLD_LINE_PATTERN}"'/s/.*/'"${NEW}"'/' "${FILE}"
    mv "${FILE}.bak" /tmp/
}
INSTANCE=zdevl
OLD_PATTERN='<context:property-placeholder location="file:///u01/deploy/zdevl/BannerAdminBPAPI/config/config.properties" />'
NEW_PATTERN='<context:property-placeholder location="file:///usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/config.properties/" />'
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
echo $OLD_PATTERN
change_line $OLD_PATTERN $NEW_PATTERN config/config.xml
#sed -i '' -e 's|env.apiPath                                                     = /u01/deploy/$INSTANCE/BannerAdminBPAPI/api-payloads/|env.apiPath                                                     = /usr/local/tomcat/webapps/BannerAdminBPAPI/WEB-INF/classes/api-payloads/'|g' config/config.properties

cd .. 


echo "Extracting BannerAdmin Business Process API"
cd BannerAdminBPAPI
jar xvf ../BannerAdminBPAPI.war
cd ..

echo "Banner Admin Business Process API is ready for configuration"
