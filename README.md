# Registration Ssb 9.10

## ENV Variables
```shell
ONLINEHELP_URL=http://HOST:PORT/banner9OH
BANNER_TRANSACTIONTIMEOUT=30
BANNER8_SS_URL=<scheme>://<server hosting Self-Service Banner 8.x>:<port>/<context root>/
SSBENABLED=true
SSBORACLEUSERSPROXIED=true
SDEENABLED=false
BANNER8_SS_STUDENTACCOUNTURL=http://<host_name>:<port_number>/<banner8ssb>/twbkwbis.P_GenMenu?name=bmenu.P_ARMnu
CAS_URL=http://CAS_HOST:PORT/cas
BANNER9_URL=http://BANNER9_HOST:PORT
BANNER9_AFTERLOGOUTURL=https://cas-server/logout?url=http://myportal/main_page.html
SSBPASSWORD_RESET_ENABLED=true
UPDATESTUDENTTERMDATA=N
GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL=http://BANNER9_HOME:PORT/StudentRegistrationSsb
DEFAULTWEBSESSIONTIMEOUT=15000
```

## build info

```shell
mkdir StudentRegistrationSsb
cd StudentRegistrationSsb
jar xvf ../StudentRegistrationSsb
cd ..
cp WEB-INF/classes/* StudentRegistrationSsb/WEB-INF/classes/
```



## Parameters configured in the database

banner.picturesPath
banner8.SS.url
banner8.SS.studentAccountUrl
grails.mail.host
grails.mail.default.from
banner.analytics.trackerId
banner.analytics.allowEllucianTracker
banner.theme.url
banner.theme.name
banner.theme.template
banner.theme.cacheTimeOut
allowPrint
updateStudentTermData