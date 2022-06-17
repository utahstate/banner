/*********************************************************************************
Copyright 2014-2021 Ellucian Company L.P. and its affiliates.
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
if(System.getenv('AUTH_METHOD') == 'saml')
{
    banner {
        sso {
            authenticationProvider           = 'saml' //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
            authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        }
    }
}
if(System.getenv('AUTH_METHOD') == 'cas')
{
    banner {
        sso {
            authenticationProvider           = 'cas' //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
            authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        }
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
grails.plugin.springsecurity.logout.afterLogoutUrl = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas') + '/logout'



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
                if(System.getenv('AUTH_METHOD') == 'cas') { active = true }
                if(System.getenv('AUTH_METHOD') == 'saml') { active = false }
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
if(System.getenv('AUTH_METHOD') == 'saml')
{
    if(System.getenv('AUTH_METHOD') == 'cas') { grails.plugin.springsecurity.saml.active = false }
    if(System.getenv('AUTH_METHOD') == 'saml') { grails.plugin.springsecurity.saml.active = true }
    grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
    grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'
    banner.sso.authentication.saml.localLogout='true' // To disable single logout set this to true,default 'false'.
    grails.plugin.springsecurity.saml.keyManager.storeFile = 'file:/usr/local/tomcat/webapps/' + (System.getenv('APP_LONG_NAME') ?: 'StudentSelfService') + '/saml/' + (System.getenv('BANNERDB') ?: 'host') + '/' + (System.getenv('BANNERDB') ?: 'host') + '_keystore.jks'  // for unix File based Example:- 'file:/home/u02/samlkeystore.jks'
    grails.plugin.springsecurity.saml.keyManager.storePass = (System.getenv('KEYSTORE_PASSWORD') ?: 'CHANGE_ME')
    grails.plugin.springsecurity.saml.keyManager.passwords = [ ((System.getenv('BANNERDB')) + '-' + (System.getenv('APP_SHORT_NAME')) + '-sp'): ((System.getenv('KEYSTORE_PASSWORD'))) ]  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
    grails.plugin.springsecurity.saml.keyManager.defaultKey = (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-sp'                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
    grails.plugin.springsecurity.saml.metadata.sp.file = '/usr/local/tomcat/webapps/' + (System.getenv('APP_LONG_NAME') ?: 'StudentSelfService') + '/saml/' + (System.getenv('BANNERDB') ?: 'host') + '/' + (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-sp.xml'     // for unix file based Example:-'/home/u02/sp-local.xml'
    grails.plugin.springsecurity.saml.metadata.providers = [adfs: '/usr/local/tomcat/webapps/' + (System.getenv('APP_LONG_NAME') ?: 'StudentSelfService') + '/saml/' + (System.getenv('BANNERDB') ?: 'host') + '/' + (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-idp.xml'] // for unix file based Example: '/home/u02/idp-local.xml'
    grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
    grails.plugin.springsecurity.saml.metadata.sp.defaults = [
            local: true,
            alias: (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-sp',                                   // banner-<short-appName>-sp is the value set in EIS Service provider setup
            securityProfile: 'metaiop',
            signingKey: (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-sp',                              // banner-<short-appName>-sp is the value set in EIS Service provider setup
            encryptionKey: (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-sp',                           // banner-<short-appName>-sp is the value set in EIS Service provider setup
            tlsKey: (System.getenv('BANNERDB') ?: 'host') + '-' + (System.getenv('APP_SHORT_NAME') ?: 'studentss') + '-sp',                                  // banner-<short-appName>-sp is the value set in EIS Service provider setup
            requireArtifactResolveSigned: false,
            requireLogoutRequestSigned: false,
            requireLogoutResponseSigned: false
    ]
}
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
seamless.footerFadeAwayTime = 0  // Footer fade away time in milliseconds. A value of less than or equal to 0 indicates that the footer will not fade away.
//seamless.defaultInfoTextLocale = "en_US" //This is an example for configuring default locale for custom text on the landing page.

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


/** ********************************************************************************
 *                                                                                 *
 *                   SS Config Dynamic Loading Job Properties                      *
 *                                                                                 *
 * Properties to set the interval and the number of times the config job would run *
 * for ConfigJob.groovy i.e. the job scheduled to update the configuration 		   *
 * properties from DB. We recommend configuring interval of the configJob in 	   *
 * such a way that it does not run as often, to help improve performance.          *
 *                                                                                 *
 * interval - in milliseonds, this is to configure the interval at which the        *
 * quartz scheduler should run. If it is not configured, the default value is 60000*
 *                                                                                 *
 * actualCount - the number of times the config job would run. If the value is -1, *
 * the job will run indefinitely. If the value is 0, the job will not run.         *
 * If not configured, the default value is -1                                      *
 *   																			   *
 ******************************************************************************** **/
configJob {
	interval = 86400000 // 24 hours
	actualCount = -1

}

/***Use this seeddata section to move required configurations to Database. **/
ssconfig.app.seeddata.keys = [['seamless.sessionTimeout': 30],['seamless.sessionTimeoutNotification': 5],['seamless.messageTimer': 60],
                                ['banner.theme.url'],
                                ['banner.theme.name': 'mytheme'],
                                ['banner.theme.template': 'applicationname'],
                                ['seamless.dbInstanceName': "Ellucian DataBase"],
                                ['productName': 'Banner General'],
                                ['banner.applicationName': 'Application Navigator']]

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
