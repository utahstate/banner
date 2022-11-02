# Communication Management 9.3.1.1

## ENV Defaults

```
ONLINEHELP_URL=http://HOST:PORT/banner9OH
BANNER_TRANSACTIONTIMEOUT=30
BANNER_PICTURESPATH=/opt/banner/images
BANNER8_SS_URL=http://<host_name>:<port_number>/<banner8>/
SSBENABLED=false
SSBORACLEUSERSPROXIED=false
SSBPASSWORD_RESET_ENABLED=true
CAS_URL=http://CAS_HOST:PORT/cas
BANNER9_URL=http://BANNER9_HOST:PORT
BANNER9_AFTERLOGOUTURL=http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/logout/customLogout
DEFAULTWEBSESSIONTIMEOUT=1500
BANNER9_HOMEPAGEURL=http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator
BANNER_ANALYSTICS_TRACKERID=
BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false
BANNER_THEME_URL=http://ThemeServer:8080/BannerExtensibility/theme
BANNER_THEME_NAME=default
BANNER_THEME_TEMPLATE=CommunicationManagement
```

## Extract War
```
mkdir CommunicationManagement
cd CommunicationManagement
jar xvf ../CommunicationManagement.war
cd ..
cp WEB-INF/classes/* CommunicationManagement/WEB-INF/classes/
```
