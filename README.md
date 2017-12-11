

## Add files to WAR
jar uvf CommunicationManagement.war WEB-INF/web.xml
jar uvf CommunicationManagement.war WEB-INF/classes/CommunicationManagement_configuration.groovy
jar uvf CommunicationManagement.war WEB-INF/classes/banner_configuration.groovy

## Environment Variables

### Banner Configuration
* ONLINEHELP_URL = http://HOST:PORT/banner9OH
* BANNER_TRANSACTIONTIMEOUT = 30
* BANNER_PICTURESPATH = /test/images
* BANNER8_SS_URL  = http://<host_name>:<port_number>/<banner8>/

### Communication Management
* SSBENABLED = true
* SSBORACLEUSERSPROXIED = true
* SSBPASSWORD_RESET_ENABLED = true

Authentication Provider Configuration
* BANNER_SSO_AUTHENICATIONPROVIDER = default
* BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE = UDC_IDENTIFIER

CAS Configuration
* GRAILS_PLUGIN_SPRINGSECURITY_SAML_ACTIVE = false
* GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE = false
* GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX = http://CAS_HOST:PORT/cas
* GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL = http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check
* GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME = http://BANNER9_HOST:PORT
* GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL = http://BANNER9_HOST:PORT/APP_NAME/secure/receptor
* GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL = https://cas-server/logout?url=http://myportal/main_page.html



Web Session Timeout
* DEFAULTWEBSESSIONTIMEOUT = 1500

Home Page link
* GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL = http://URL:PORT/

Theme Configuration
* BANNER_THEME_URL = http://BANNER9_HOST:PORT/BannerExtensibility/theme
* BANNER_THEME_NAME = <THEME_NAME>
* BANNER_THEME_TEMPLATE = <THEME_TEMPLATE_NAME>
