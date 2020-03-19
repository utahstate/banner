/*******************************************************************************
 Copyright 2017-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/******************************************************************************
 This file contains configuration needed by the Banner XE Page Builder
 web application. Please refer to the administration guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:
 * PageBuilder
     *   Enable/Disable switch and DataSource connection details for Page Builder artifacts
     *   Miscellaneous configuration

 * Theme
     *   url:  theme server url, if this is not specified then it points to same app
     *   theme: used by client application
     *   template: used by client application
     *   cacheTimeOut: themes would be cached for specified duration (in seconds) in theme server
                       (This is not required if the app points to remote theme server)

 * Self Service Support
 * CAS SSO Configuration (supporting self service users)

 NOTE: Banner DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.

 *******************************************************************************/

pageBuilder.enabled = true

if (!pageBuilder.enabled) {
  grails.plugin.springsecurity.securityConfigType = grails.plugin.springsecurity.SecurityConfigType.InterceptUrlMap
}

/*******************************************************************************
 *                                                                              *
 *              Page Builder Artifact File Location Configuration               *
 *                                                                              *
 *******************************************************************************/
pbRoot = (System.getenv('PB_ROOT')?: '/opt/banner/extensibilty/pb')
pageBuilder {
	locations {
	  bundle        = "${pbRoot}/i18n"
	  page          = "${pbRoot}/page"
	  css           = "${pbRoot}/css"
	  virtualDomain = "${pbRoot}/virtdom"
	}
    // Uncomment debugRoles to reveal detailed SQL error messages for
    // Virtual domains to users with any of the comma separated roles
    // debugRoles = "ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M"
}

/*******************************************************************************
 *                                                                              *
 *              Theme Configuration                                             *
 *                                                                              *
 *******************************************************************************/
environments {
     production {
         banner.theme.url="http://BANNER9_HOST:PORT/BannerExtensibility/theme"   // required only if theme server is remote
         banner.theme.name="production"
         banner.theme.template="BannerExtensibility-9_5_0_1"
         banner.theme.cacheTimeOut = 900                                    // in seconds, not required theme server is remote
     }
     development {
         banner.theme.url="http://BANNER9_HOST:PORT/BannerExtensibility/theme"  // required only if theme server is remote
         banner.theme.name="development"
         banner.theme.template="BannerExtensibility-9_5_0_1"
         banner.theme.cacheTimeOut = 120                                   // // in seconds, not required theme server is remote
         //This variable is used to get information about $$user authorities(Roles). This should be used only for Development, shouldn't be available in prod. by default it should be false.
         pageBuilder.development.authorities.enabled=false
     }
}

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************


ssbEnabled = true
ssbOracleUsersProxied = true
ssbPassword.reset.enabled = false //true  - allow Pidm users to reset their password.
                                 //false - throws functionality disabled error message


/** *****************************************************************************
 *                                                                              *
 *                AUTHENTICATION PROVIDER CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default, cas or saml.
// If using cas or saml, Either the CAS CONFIGURATION or the SAML CONFIGURATION
// will also need configured/uncommented as well as set to active.
//


banner {
   sso {
        authenticationProvider = 'cas' //  Valid values are: 'saml' and 'cas' for SSO. 'default' value to be used only when creating the release zip file.
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
    }
}

if (banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' ) {
   grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
}

// ******************************************************************************
//
//                       +++ CAS CONFIGURATION +++
//
// ******************************************************************************

grails {
   plugin {
        springsecurity {
            cas {
                active = true
                serverUrlPrefix = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/BannerExtensibility/login/cas'
                serverName = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') 
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/BannerExtensibility/secure/receptor'
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                serverUrlEncoding = 'UTF-8'
                filterProcessesUrl = '/login/cas'
                if (useSingleSignout){
                   grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                afterLogoutUrl = (System.getenv('BANNER9_AFTERLOGOUTURL') ?: 'https://cas-server/logout?service=http://myportal/main_page.html')
                // afterLogoutUrl = '/' // This can be used to navigate to the landing page when not using CAS
                mepErrorLogoutUrl = '/logout/logoutPage'
            }
        }
    }
}


grails.plugin.springsecurity.homePageUrl= 'http://BANNER9_HOST:PORT/APP_NAME/'

//This setting contains the institution-specific redirect URL for MEP if Return Home is clicked.
grails.plugin.springsecurity.logout.mepErrorLogoutUrl = '/logout/customLogout'

//guestAuthenticationEnabled = true

// This entry is required to ensure that 'Sign In' link takes you to corresponding login i.e. for CAS,
// any other authentication system or Default (Banner).
// Example navigates to home page after sign in
loginEndpoint='http://BANNER9_HOST:PORT/APP_NAME/customPage/page/pbadm.ssoauth?url=/'


/** *************************************************************************************
 *                                                                                      *
 *                        SAML CONFIGURATION                                            *
 *  Un-comment the below code and set active = true when authentication mode is saml.   *
 * keep the below saml configuration commented for all other login such as default      *
 * and CAS to avoid Deployment failures                                                 *
 *                                                                                      *
 ************************************************************************************* **/

/*******************************************************************************
                            SAML2 SSO CONFIGURATION
NOTE:   Uncomment Below saml configuration only for SAML login.
        keep the below saml configuration commented for all other login such as default and CAS to avoid Deployment failures.
***************************************************************************** **/
// set active = true when authentication provider section configured for saml
/*
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.auth.loginFormUrl = ‘/saml/login’
grails.plugin.springsecurity.saml.afterLogoutUrl =‘/logout/customLogout’
banner.sso.authentication.saml.localLogout=‘false’                                                    // To disable single logout set this to true,default ‘false’.
grails.plugin.springsecurity.saml.keyManager.storeFile = ‘classpath:security/<KEY_NAME>.jks’          // for unix File based Example:- ‘file:/home/u02/samlkeystore.jks’
grails.plugin.springsecurity.saml.keyManager.storePass = ‘test1234’
grails.plugin.springsecurity.saml.keyManager.passwords = [ ‘banner-<short-appName>-sp’: ‘test1234’ ]  // banner-<short-appName>-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = ‘banner-<short-appName>-sp’                 // banner-<short-appName>-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.metadata.sp.file = ‘security/banner-<Application_Name>-sp.xml’     // for unix file based Example:-‘/home/u02/sp-local.xml’
grails.plugin.springsecurity.saml.metadata.providers = [adfs: ‘security/banner-<Application_Name>-idp.xml’] // for unix file based Example: ‘/home/u02/idp-local.xml’
grails.plugin.springsecurity.saml.metadata.defaultIdp = ‘adfs’
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
       local: true,
       alias: ‘banner-<short-appName>-sp’,                                   // banner-<short-appName>-sp is the value set in EIS Service provider setup
       securityProfile: ‘metaiop’,
       signingKey: ‘banner-<short-appName>-sp’,                              // banner-<short-appName>-sp is the value set in EIS Service provider setup
       encryptionKey: ‘banner-<short-appName>-sp’,                           // banner-<short-appName>-sp is the value set in EIS Service provider setup
       tlsKey: ‘banner-<short-appName>-sp’,                                  // banner-<short-appName>-sp is the value set in EIS Service provider setup
       requireArtifactResolveSigned: false,
       requireLogoutRequestSigned: false,
       requireLogoutResponseSigned: false
]
*/

/*******************************************************************************
 *                                                                              *
 *              X-Frame-Options                                                 *
 *                                                                              *
 *******************************************************************************/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


/*******************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 *******************************************************************************/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']


/************************************************************
             Web Application Extensibility
************************************************************/
webAppExtensibility {
    // Comma separated list of roles
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}

/************************************************************
                    SS Config Changes
************************************************************/
configJob {
    delay = 60000
    interval = 60000
    actualCount = -1
    //actualCount will be the count how many times the configJob would run.
}


/************************************************************
                   Disabling Loacle for self service
************************************************************/

locale=false

ssconfig.app.seeddata.keys = [['banner.analytics.allowEllucianTracker'], ['banner.analytics.trackerId'], ['banner.applicationName'],
 ['banner.theme.cacheTimeOut'], ['banner.theme.name'], ['banner.theme.template'], ['banner.theme.url'],
 ['loginEndpoint'], ['pageBuilder.enabled'], ['productName'], ['ssbEnabled'], ['ssbOracleUsersProxied'], ['locale']]


 /** ********************************************************************************
 *                                                                                 *
 * Google Analytics                                                                *
 * banner.analytics.trackerId=[institution's analytics tracker ID - default blank] *
 * banner.analytics.allowEllucianTracker=[true|false - default true]               *
 * Added as part of Platform Platform 9.20                                         *
 ******************************************************************************** **/
banner.analytics.trackerId=""     // institution's analytics tracker ID - blank by default
banner.analytics.allowEllucianTracker=false


/** *************************************************************************************************************
 * Response Headers                                                                                				*
 * 																   												*
 * This is the map which takes the "header property" as key and value as shown in below example					*
 * responseHeaders = [ "X-Content-Type-Options": "nosniff" , "X-XSS-Protection": "1; mode=block" ...]			*
 * Added as part of Platform Platform 9.32                                         							    *
 ************************************************************************************************************* **/
responseHeaders =[
    "X-Content-Type-Options": "nosniff",
    "X-XSS-Protection": "1; mode=block"
]

productName="Banner General"
banner.applicationName="BannerExtensibility"


/*********************************************************************************
*                     Application Server Configuration                          *
* When deployed on Tomcat this configuration should be targetServer="tomcat"    *
* When deployed on Weblogic this configuration should be targetServer="weblogic"*
*********************************************************************************/
targetServer="tomcat"


/********************************************************************************
*                                                                               *
* AboutInfoAccessRoles will help to check whether user is allowed to view       *
* the platform version                                                          *
*                                                                               *
*********************************************************************************/
aboutInfoAccessRoles = ["ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"]
