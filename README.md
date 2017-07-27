

## Add files to WAR
jar uvf StudentRegistrationSsb.war WEB-INF/classes/banner_configuration.groovy
jar uvf StudentRegistrationSsb.war WEB-INF/classes/StudentRegistrationSsb_configuration.groovy
jar uvf StudentRegistrationSsb.war WEB-INF/web.xml




BANNER_TRANSACTIONTIMEOUT = 30
BANNER_PICTURESPATH = '/test/images'
BANNER8_SS_URL = '<scheme>://<server hosting Self-Service Banner 8.x>:<port>/<context root>/'

SSBENABLED = true
SSBORACLEUSERSPROXIED = false
SDEENABLED = false
BANNER8_SS_STUDENTACCOUNTURL = "http://<host_name>:<port_number>/<banner8ssb>/twbkwbis.P_GenMenu?name=bmenu.P_ARMnu"

BANNER_SSO_AUTHENTICATIONPROVIDER = default
GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE = false
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX = 'http://CAS_HOST:PORT/cas'
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL = 'http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check'
GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME = 'http://BANNER9_HOST:PORT'
GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL = 'http://BANNER9_HOST:PORT/APP_NAME/secure/receptor'
GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT = 'https://cas-server/logout?url=http://myportal/main_page.html'

GRAILS_MAIL_HOST = "your.smtp.address"
GRAILS_MAIL_DEFAULT_FROM = "your_email_address@school.edu"
ALLOWPRINT = true
SSBPASSWORD_RESET_ENABLED = true

UPDATESTUDENTTERMDATA = 'N'

GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL = 'http://URL:PORT/'

DEFAULTWEBSESSIONTIMEOUT = 15000

BANNER_THEME_URL = http://ThemeServer:8080/pathTo/ssb/theme
BANNER_THEME_NAME = default
BANNER_THEME_TEMPLATE = all

BANNER_ANALYTICS_TRACKERID = ''
BANNER_ANALYTICS_ALLOWELLUCIANTRACKER = true
