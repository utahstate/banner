# Registration Ssb 9.9

## ENV Variables
```shell
ONLINEHELP_URL=http://HOST:PORT/banner9OH
BANNER_TRANSACTIONTIMEOUT=30
BANNER_PICTUREPATH=/opt/banner/images
BANNER8_SS_URL=<scheme>://<server hosting Self-Service Banner 8.x>:<port>/<context root>/
SSBENABLED=true
SSBORACLEUSERSPROXIED=true
SDEENABLED=false
BANNER8_SS_STUDENTACCOUNTURL=http://<host_name>:<port_number>/<banner8ssb>/twbkwbis.P_GenMenu?name=bmenu.P_ARMnu
CAS_URL=http://CAS_HOST:PORT/cas
BANNER9_URL=http://BANNER9_HOST:PORT
BANNER9_AFTERLOGOUTURL=https://cas-server/logout?url=http://myportal/main_page.html
GRAILS_MAIL_HOST=mailhost.sct.com
GRAILS_MAIL_DEFAULT_FROM=firstname.lastname@ellucian.com
ALLOWPRINT=true
SSBPASSWORD_RESET_ENABLED=true
UPDATESTUDENTTERMDATA=N
GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL=http://BANNER9_HOME:PORT/StudentRegistrationSsb
DEFAULTWEBSESSIONTIMEOUT=15000
BANNER_THEME_URL=http://ThemeServer:8080/pathTo/ssb/theme
BANNER_THEME_NAME=default
BANNER_THEME_TEMPLATE=all
BANNER_ANALYSTICS_TRACKERID=
BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=true
```

## build info

```shell
mkdir StudentRegistrationSsb
cd StudentRegistrationSsb
jar xvf ../StudentRegistrationSsb
cd ..
cp WEB-INF/classes/* StudentRegistrationSsb/WEB-INF/classes/
```

