# Student Registration SSB config files with env parsing

## Build

To build this example you will need to extract the StudentRegistrationSSB.war file into the directory StudentRegistrationSSB.  Move the config files from WEB-INF/classes into the extrated StudentRegistrationSSB directory overwriting the delivered files.  You can also update the files manually allowing to changes to the delivered config files.  

Any of the listed below environment variables can be updated at runtime.  

## ENV Defaults

```Shell
CATALINA_HOME=/usr/local/tomcat
ONLINEHELP_URL=http://HOST:PORT/banner9OH
BANNER_TRANSACTIONTIMEOUT=30
BANNER_PICTUREPATH=/test/images
BANNER8_SS_URL=<scheme>://<server hosting Self-Service Banner 8.x>:<port>/<context root>/
SSBENABLED=true
SSBORACLEUSERSPROXIED=true
SDEENABLED=false
BANNER8_SS_STUDENTACCOUNTURL=http://<host_name>:<port_number>/<banner8ssb>/twbkwbis.P_GenMenu?name=bmenu.P_ARMnu
BANNER_SSO_AUTHENTICATIONPROVIDER=default
BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE=UDC_IDENTIFIER
GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE=false
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL=http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check
GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL=http://BANNER9_HOST:PORT/APP_NAME/secure/receptor
GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT=true
GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL=https://cas-server/logout?url=http://myportal/main_page.html
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

## Run with Docker Compose

```Shell
docker-compose up -d
```