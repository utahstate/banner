/*******************************************************************************
 Copyright 2017-2021 Ellucian Company L.P. and its affiliates.
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
         banner.theme.template="BannerExtensibility-9_11"
         banner.theme.cacheTimeOut = 900                                    // in seconds, not required theme server is remote
     }
     development {
         banner.theme.url="http://BANNER9_HOST:PORT/BannerExtensibility/theme"  // required only if theme server is remote
         banner.theme.name="development"
         banner.theme.template="BannerExtensibility-9_11"
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
                if(System.getenv('AUTH_METHOD') == 'cas') { active = true }
                if(System.getenv('AUTH_METHOD') == 'saml') { active = false }
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

/** *************************************************************************************
 *                                                                                      *
 *                        SAML CONFIGURATION                                            *
 *  Un-comment the below code and set active = true when authentication mode is saml.   *
 * keep the below saml configuration commented for all other login such as default      *
 * and CAS to avoid Deployment failures                                                 *
 *                                                                                      *
 ************************************************************************************* **/

// set active = true when authentication provider section configured for saml

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
    grails.plugin.springsecurity.saml.metadata.defaultIdp = (System.getenv('IDP_URL') ?: 'https://sts.windows.net/ac352f9b-eb63-4ca2-9cf9-f4c40047ceff/')
    grails.plugin.springsecurity.saml.maxAuthenticationAge = (System.getenv('MAX_AUTH_AGE') ?: 43200)
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

/* Set feature.enableConfigJob to true for configJob to run as configured and
set feature.enableConfigJob to false for configJob to NOT run as configured */

feature.enableConfigJob = true

/* Set feature.enableApplicationPageRoleJob to true for applicationPageRoleJob to run as configured and
set feature.enableApplicationPageRoleJob to false for applicationPageRoleJob to NOT run as configured */

feature.enableApplicationPageRoleJob = false

/** ********************************************************************************
 *                                                                                 *
 *                   SS Config Dynamic Loading Job Properties                      *
 *                                                                                 *
*                   Cron Expressions:                                             *
 *                                                                                 *
 *                   ┌───────────── second (0-59)                                  *
 *                   │ ┌───────────── minute (0 - 59)                              *
 *                   │ │ ┌───────────── hour (0 - 23)                              *
 *                   │ │ │ ┌───────────── day of the month (1 - 31)                *
 *                   │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)            *
 *                   │ │ │ │ │ ┌───────────── day of the week (0 - 7)              *
 *                   │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)         *
 *                   │ │ │ │ │ │                                                   *
 *                   * * * * * *                                                   *
 *                                                                                 *
 ******************************************************************************** **/
/*ConfigJob - the job scheduled to update the configuration properties from DB
ApplicationPageRoleJob - the job scheduled to update the interceptedUrlMap from DB. */

configJob {
    // Recommended default is every 1 hour starting at 00am, of every day - "0 0 */1 * * ?"
    // Cron expression lesser than 30 mins will fall back to 30 mins.
    cronExpression = "0 0 */1 * * ?"
}
applicationPageRoleJob {
    // Recommended default is once at 00:00:00am every day - "0 0 0 * * ?"
    // Cron expression lesser than 30 mins will fall back to 30 mins.
    cronExpression = "0 0 0 * * ?"
}


/************************************************************
                   Disabling Loacle for self service
************************************************************/

locale=false

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

/*********************************************************************************
*                     Application Server Configuration                          *
* When deployed on Tomcat this configuration should be targetServer="tomcat"    *
* When deployed on Weblogic this configuration should be targetServer="weblogic"*
*********************************************************************************/
targetServer="tomcat"

/** *******************************************************************************
 *                              enableNLS                                         *
 * Setting it to true will set National Language support in the Oracle database   *
 * to the user specific language, that is the error messages from Oracle database *
 * will be in the user specific language, while setting it to false would disable *
 * the Nation Language support for the error messages from Oracle database and    *
 * improves the performance of the application.                                   *
 ******************************************************************************* **/
enableNLS=true

/**************************************************************************************
* List of allowed domains configuration for Ellucian Experience                       *
* Do not change this configuration unless instructed.                                 *
* Do not move this configuration to Banner Applications Configurations (GUACONF) page.*
************************************************************************************* **/

allowedExperienceDomains=[
"https://experience-test.elluciancloud.com",
"https://experience.elluciancloud.com",
"https://experience-test.elluciancloud.ca",
"https://experience.elluciancloud.ca",
"https://experience-test.elluciancloud.ie",
"https://experience.elluciancloud.ie",
"https://experience-test.elluciancloud.com.au",
"https://experience.elluciancloud.com.au"]