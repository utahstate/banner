/** *****************************************************************************
 Copyright 2011-2024 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */


/*******************************************************************************

 This file contains configuration needed by the Self-Service Banner 9 Registration
 web application. Please refer to the administration guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:

 * Self Service Support

 * CAS SSO Configuration (supporting administrative and self service users)

 NOTE: DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.

 ***************************************************************************** **/

grails.mail.host = 'mail.usu.edu'

/** *****************************************************************************
 *                                                                              *
 *                         Self Service Support                                 *
 *                                                                              *
 ***************************************************************************** **/

 /** *********************************************************************************
  Set 'isExperienceIntegrated' to true for accessing the SSB application only in
  Experience. Set to false to access the SSB application in standalone mode.
  Default value is 'false'
************************************************************************************ */
isExperienceIntegrated = false

/** *********************************************************************************
  Set 'ssbEnabled' to true for instances that expose Self Service Banner endpoints.
  If this is set to false, or if this configuration item is missing, the instance
  will only support administrative users and not self service users.
  If this is enabled, it is important to also ensure the corresponding configuration
  items for the SSB datasource are configured.
  Default value is 'false'
************************************************************************************ */
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)

/** *****************************************************************************
 *                                                                              *
 *                        OAuth2 configuration                               *
 *                                                                              *
 ***************************************************************************** **/
banner.oauth2.issuerJwksURi= "https://oauth.prod.10005.elluciancloud.com/jwks"
banner.oauth2.issuer = "https://oauth.prod.10005.elluciancloud.com"
banner.oauth2.audiance="https://elluciancloud.com"

/***********************************************************************************
  Set 'ssbOracleUsersProxied = true' to ensure that database connections are proxied
  when the user has an oracle account.  This allows FGAC even for SSB pages.
  Set this to false to instead use database connections that are established
  for SSB users who do not have Oracle database accounts.
  This setting applies only to SSB pages.
************************************************************************************ */
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)

/** *****************************************************************************
 *                                                                              *
 *               Supplemental Data Support Enablement                           *
 *                                                                              *
 ***************************************************************************** **/
// Default is false for ssbapplications.
sdeEnabled=(System.getenv('SDEENABLED') ? Boolean.parseBoolean(System.getenv('SDEENABLED')): false )

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
boolean ssoEnabled = false
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

if(banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' ) {
    ssoEnabled = true
}
if(ssoEnabled)
{
    grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
}

/** *****************************************************************************
 *                                                                              *
 *                             CAS CONFIGURATION                                *
 *                                                                              *
 ***************************************************************************** **/
// set active = true when authentication provider section configured for cas
grails {
    plugin {
        springsecurity {
            cas {
                if(System.getenv('AUTH_METHOD') == 'cas') { active = true }
                if(System.getenv('AUTH_METHOD') == 'saml') { active = false }
                serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/StudentRegistrationSsb/login/cas'
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/StudentRegistrationSsb/APP_NAME/secure/receptor'
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
                afterLogoutUrl = '/logout/customLogout'
            }
        }
    }
}


/** *****************************************************************************
 *                                                                              *
 *                        SAML CONFIGURATION                                    *
 *        Un-comment the below code when authentication mode is saml.           *
 *                                                                              *
 ***************************************************************************** **/
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

/** *****************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

/** *****************************************************************************
 *                                                                              *
 * The errors reported by YUI in each of these files are because YUI            *
 * compressor/minifier does not support ES5 – the version of JavaScript         *
 * incorporated in browsers since IE9.   Specifically, ES5 allows use of JS     *
 * reserved words as property names with the ‘.NAME’ syntax(e.g., “object.case”)*
 * This results in a syntax error in YUI minifier, but is legal ES5 syntax.     *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.mappers.yuijsminify.excludes = ['**/*.min.js','**/angularjs-color-picker.js', '**/m.js', '**/bundle-aurora_defer.js']

/** *****************************************************************************
 *                                                                              *
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://BANNER9_HOME:PORT/StudentRegistrationSsb' )


/** *****************************************************************************
 *                                                                              *
 *   This setting is needed if the application needs to work inside             *
 *   Application Navigator and the secured application pages will be accessible *
 *   as part of the single-sign on solution.                                    *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


/** *****************************************************************************
 *                                                                              *
 *                      ConfigJob (Platform 9.23)                               *
 *     Support for configurations to reside in the database.                    *
 *     Platform 9.33.2.1 removes support for delay                              *
 *     Platform 9.34 changes interval to 24 hours.                              *
 *     Platform 9.39 major changes to cron format.                              *
 ***************************************************************************** **/
//configJob {
//    interval = 86400000 // 24 hours
//    actualCount = -1
//    }
/* Set feature.enableConfigJob to true for configJob to run as configured and
set feature.enableConfigJob to false for configJob to NOT run as configured */

feature.enableConfigJob = true

/* Set feature.enableApplicationPageRoleJob to true for applicationPageRoleJob to run as configured and
set feature.enableApplicationPageRoleJob to false for applicationPageRoleJob to NOT run as configured */

feature.enableApplicationPageRoleJob = true

/** ********************************************************************************
 *                                                                                 *
 *                   SS Config Dynamic Loading Job Properties                      *
 *                                                                                 *
 *                   Cron Expressions:                                             *
 *                                                                                 *
 *                   ┌───────────── second (0-59)                                  *
 *                   │ ┌───────────── minute (0 - 59)                            *
 *                   │ │ ┌───────────── hour (0 - 23)                              *
 *                   │ │ │ ┌───────────── day of the month (1 - 31)                  *
 *                   │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)            *
 *                   │ │ │ │ │ ┌───────────── day of the week (0 - 7)            *
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
 Extensibility extensions & i18n file location
 ************************************************************/

 webAppExtensibility {
    locations {
       extensions = "/home/oracle/config_extn/ssb/extensions"
       resources  = "/home/oracle/config_extn/ssb/i18n"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
 }

/** *****************************************************************************
 *                                                                              *
 *                           MEP Redirect                                       *
 *             (Applicable for MEP environments only)                           *
 *                                                                              *
 ***************************************************************************** **/
//grails.plugin.springsecurity.logout.mepErrorLogoutUrl='http://myportal/main_page.html'

/********************************************************************************
*                                                                               *
*                           Target Server                                       *
********************************************************************************/
targetServer="tomcat"

/** *************************************************************************************************************
 * Response Headers                                                                                				*
 * 																   												*
 * This is the map which takes the "header property" as key and value as shown in below example					*
 * responseHeaders = [ "X-Content-Type-Options": "nosniff" , "X-XSS-Protection": "1; mode=block" ...]			*
 * Added as part of Platform Platform 9.32                                         							    *
 ************************************************************************************************************* **/
responseHeaders = ["X-Content-Type-Options": "nosniff","X-XSS-Protection": "1; mode=block"]


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

/** *****************************************************************************
                                                                         *
            Text Manager Configuration                                   *
                                                                         *
***************************************************************************** */
/*
Below configurations are required for an application in order to enable Text Manager Translations
    * enableTextManagerTranslations
        To Enable Text Manager translations, set to false if its not required for an application.
        setting it to false completely disables the translations from Text Manager in both MEP and Non-MEP environment

    * enableTextManagerTranslationsInMEP
        To Enable Text Manager translations in MEP environment for an application.
        set to true if the TextManager tables are MEPed and Translations are required as per institution.
*/
enableTextManagerTranslations = true
enableTextManagerTranslationsInMEP = false
