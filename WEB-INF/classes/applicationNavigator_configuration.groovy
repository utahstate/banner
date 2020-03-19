/*********************************************************************************
Copyright 2014-2020 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 
 /*******************************************************************************
 *                                                                              *
 *              Application Navigator Configuration                             *
 *                                                                              *
 ********************************************************************************/

/*
This file contains configuration needed by the Application Navigator
web application. Please refer to the installation guide for additional
information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)
    
    * Administrative and Self Service Endpoint Support Enablement
         
    * CAS / SAML2 SSO Configuration (supporting administrative and self serivce users)

    * Application Navigator seamless plugin configurations

    * Application Navigator Display Name, MEP and X-Frame-Options configurations
    
     NOTE: DataSource and JNDI configuration resides in the cross-module 
           'banner_configuration.groovy' file. 
    
********************************************************************************/
/********************************************************************************
 *                                                                              *
 *               Administrative Endpoint Support Enablement                     *
 *                                                                              *
 ********************************************************************************/
// Disabling 'administrativeBannerEnabled' (setting to 'false') will prevent the 
// BannerAuthenticationProvider from attempting to authenticate users. 
//
//administrativeBannerEnabled = false  // default is 'true'


/********************************************************************************
 *                                                                              *
 *                Self Service Endpoint Support Enablement                      *
 *                                                                              *
 ********************************************************************************/
// Set 'ssbEnabled' to true for instances that expose Self Service Banner endpoints. 
// If this is set to false, or if this configuration item is missing, the instance 
// will only support administrative applications and not self service applications
// in the unified menu. 
//
// If this is enabled, Application Navigator will integrate with Banner Self Service
// applications using the SSB datasource. It is important to also ensure the 
// corresponding commonSelfServiceMenu menu endpoint is configured below. 
ssbEnabled = true
myLinksEnabled = true

// This setting is set to false for Application Navigator deployment by default.
// Only set 'ssbOracleUsersProxied = true' to ensure that database connections
// are proxied when the SS user has an oracle account and there is a strong need
// to set FGAC for Application Navigator menus.
//
// This setting in Application Navigator has no impact on integrated Banner 9 Self-
// Service applications. The integrated Banner 9 Self-Service applications can be
// configured separately to allow FGAC on the application specific SSB pages.
ssbOracleUsersProxied = false


/********************************************************************************
 *                                                                              *
 *                Banner Authentication Provider Configuration                  *
 *                                                                              *
 ********************************************************************************/
//
banner {
    sso
    {
    authenticationProvider = 'cas'
    authenticationAssertionAttribute = 'UDC_IDENTIFIER'
    }
}
if(banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' )
    {
    grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
    }

/********************************************************************************
 *                                                                              *
 *                Application Navigator Logout URL                              *
 *                                                                              *
 ********************************************************************************/
 // When the banner sso authentication provider is set to "cas", set the value of
 // the afterLogoutUrl property to:
 //  'https://CAS_HOST:PORT/cas/logout?url=http://APPLICATION_NAVIGATOR_HOST:PORT/'
 // For other authentication providers use the setting noted below.
grails.plugin.springsecurity.logout.afterLogoutUrl = 'http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/logout/customLogout'



/********************************************************************************
 *                                                                              *
 *           Home Page URL configuration for CAS / SAML Single-Sign On          *
 *                                                                              *
 ********************************************************************************/
// Can be institutional home page ex: http://myportal/main_page.html
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/' )


/********************************************************************************
 *                                                                              *
 *                             CAS SSO Configuration                            *
 *                                                                              *
 ********************************************************************************/
// Set active = true when Application Navigator is configured for CAS SSO  
grails {
    plugin {
        springsecurity {
            cas {
                active           = true
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/applicationNavigator/login/cas"
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/applicationNavigator/secure/receptor"
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/login/cas'
                serverUrlEncoding = 'UTF-8'
                if (useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
        }
    }
}

// Cannot be declared bean style because the value is not available for reference in seamless.interceptPattern
grails.plugin.springsecurity.cas.serverUrlPrefix = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')


/********************************************************************************
 *                                                                              *
 *                         SAML2 SSO Configuration                              *
 *                                                                              *
 ********************************************************************************/
//Set active = true when Application Navigator is configured for SAML2 SSO
/*grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.auth.loginFormUrl = ‘/saml/login’
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'
banner.sso.authentication.saml.localLogout='false'	 // Setting localLogout to false, allows the application to send a single or global logout request to the Identity Service Provider
grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/samlkeystore.jks'     // for unix based 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-appnav-sp': 'changeit' ]        // banner-appnav-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-appnav-sp'                       // banner-appnav-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-appnav-sp.xml'               // for unix based '/home/u02/banner-appnav-sp.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security//banner-appnav-idp.xml']   // for unix based '/home/u02/banner-appnav-idp.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
    local: true,
    alias: 'banner-appnav-sp',                                                                 // banner-appnav-sp is the value set in EIS Service provider setup
    securityProfile: 'metaiop',
    signingKey: 'banner-appnav-sp',                                                            // banner-appnav-sp is the value set in EIS Service provider setup
    encryptionKey: 'banner-appnav-sp',                                                         // banner-appnav-sp is the value set in EIS Service provider setup
    tlsKey: 'banner-appnav-sp',                                                                // banner-appnav-sp is the value set in EIS Service provider setup
    requireArtifactResolveSigned: false,
    requireLogoutRequestSigned: false,
    requireLogoutResponseSigned: false
]*/
/********************************************************************************
 *                                                                              *
 *              Application Navigator Seamless plugin Configuration             *
 *                                                                              *
 ********************************************************************************/
//
seamless.interceptPattern = "${grails.plugin.springsecurity.cas.serverUrlPrefix}.*"

// When ssbEnabled is set to true, the commonSelfServiceMenu menu endpoint will be 
// invoked along with the administrative commonMenu menu endpoint when loading the 
// menus.
seamless.menuEndpoints = [
(System.getenv('BANNER9_URL')?: 'http://APPLICATION_NAVIGATOR_HOST:PORT') + "/applicationNavigator/commonMenu",
(System.getenv('BANNER9_URL')?: 'http://APPLICATION_NAVIGATOR_HOST:PORT') + "/applicationNavigator/commonSelfServiceMenu"
]

// List the URL entries of Banner Self Service Applications integrating with Application
// Navigator.
//
// If Application Navigator is configured with a MEP Database, then the URLs of the
// Self Service Applications listed below must be appended with the MEP code query
// parameter as shown below:
//   "http://SELFSERVICE_APPLICATION_HOST:PORT/<BANNER_SSB_APPLICATION>?mepCode={mepCode}"
//
// The entries added must match those entries listed in the Web Tailor menus without
// which they will not be displayed in the Application Navigator unified menu.
seamless.selfServiceApps = [
(System.getenv("BANNER9_SS_URL") ?: 'http://APPLICATION_NAVIGATOR_HOST:PORT') + "/BannerExtensibility/"
]

seamless.logLevel="off"
seamless.ajaxTimeout=30000
seamless.messageResponseTimeout=2000
seamless.exposeMenu=true

// Configure the brand title with a default institution value or based on the MEP institution code configured in the Banner database
// To add values by MEP code, append the name:value pair to the existing seamless.brandTitle property. Example
// Example: seamless.brandTitle=["Default": "Ellucian University","<MEP_CODE>":"<MEP_BRAND_TITLE>", "<MEP_CODE>":"<MEP_BRAND_TITLE>"]
seamless.brandTitle=["Default": "Ellucian University"]

/********************************************************************************
 *                                                                              *
 *                    Application Navigator MEP Configuration                   *
 *                                                                              *
 ********************************************************************************/
//
mepEnabled = false
grails.plugin.springsecurity.logout.mepErrorLogoutUrl = '/logout/customLogout'

/********************************************************************************
 *                                                                              *
 *              Application Navigator X-Frame-Options Configuration             *
 *                                                                              *
 ********************************************************************************/
// Setting the X-Frame-Options will not expose Application Navigator login page  
// to the clickjacking vulnerability when loaded in an iframe.
grails.plugin.xframeoptions.deny = true
grails.plugin.xframeoptions.urlPattern = '/login/auth'


/********************************************************************************
 *                                                                              *
 *              Spring Security Port Forwarding Configuration                   *
 *                                                                              *
 ********************************************************************************/
// To support port forwarding for the menu service callback URLs,
// uncomment the spring security port mapper configuration shown below
// and specify the port where the HTTP(S) request must be forwarded to.

// grails.plugin.springsecurity.portMapper.httpPort = <PORT_NUMBER>
// grails.plugin.springsecurity.portMapper.httpsPort = <SSL_PORT_NUMBER>


/********************************************************************************
*                                                                              *
*              SS -Config Configuration                                        *
*                                                                              *
********************************************************************************/
configJob.delay = 60000
configJob.interval = 120000
configJob.actualCount = -1

/***Use this seeddata section to move required configurations to Database. **/
ssconfig.app.seeddata.keys = [['seamless.sessionTimeout': 30],['seamless.sessionTimeoutNotification': 5],['seamless.messageTimer': 60],
                                ['banner.analytics.trackerId': ''],['banner.analytics.allowEllucianTracker': true],
                                ['banner.theme.url'],
                                ['banner.theme.name': 'mytheme'],
                                ['banner.theme.template': 'applicationname'],
                                ['seamless.dbInstanceName': "Ellucian DataBase"],
                                ['productName': 'Banner General'],
                                ['banner.applicationName': 'Application Navigator'],
                                ['grails.plugin.springsecurity.logout.afterLogoutUrl'],
                                ['grails.plugin.springsecurity.logout.mepErrorLogoutUrl']]

/********************************************************************************
*              Theming Configuration                                            *
********************************************************************************/
banner.theme.url="https://theme.elluciancloud.com/<AccountApiID>/theme"


/********************************************************************************
*                     NLS Configuration                                         *
********************************************************************************/
enableNLS = false


/*********************************************************************************
*                     Application Server Configuration                    *
* When deployed on Tomcat this configuration should be targetServer="tomcat"     *
* When deployed on Weblogic this configuration should be targetServer="weblogic" *
**********************************************************************************/
targetServer="tomcat"
