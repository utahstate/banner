## Banner Configuration
ONLINEHELP_URL = http://HOST:PORT/banner9OH
BANNER_TRANSACTIONTIMEOUT = 30
BANNER_PICTURESPATH = /test/images
BANNER8_SS_URL = http://<host_name>:<port_number>/<banner8>/

## AppNav Configuration
CATALINA_HOME
SSBENABLED = true
SSBORACLEUSERSPROXIED = true

### SSO Configuration
BANNER_SSO_AUTHENTICATIONPROVIDER = default
BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE = UDC_IDENTIFIER
GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL = http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/logout/customLogout
GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL = http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator
GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE = false
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL = http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME = http://BANNER9_HOST:PORT
GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL = http://BANNER9_HOST:PORT/APP_NAME/secure/receptor
GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT = true
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX = http://CAS_HOST:PORT/cas


BANNER_SS_BASE_URL = https://SELFSERVICE_APPLICATION_HOST
SEAMLESS_BRANDTITLE = Ellucian University
SEAMLESS_SESSIONTIMEOUT = 30
SEAMLESS_SESSIONTIMEOUTNOTIFICATION = 5