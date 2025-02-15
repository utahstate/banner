/*********************************************************************************
 Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/

/** *****************************************************************************
 *                                                                              *
 *         Banner 9 Employee Self-Service Configuration                         *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

This file contains configuration needed by the Self-Service Banner 9 Employee
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * Self Service Support

    * CAS SSO Configuration (supporting administrative and self service users)

     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.

***************************************************************************** **/

/** *****************************************************************************
 *                                                                              *
 *                        SELF SERVICE SUPPORT                                  *
 *                                                                              *
 ***************************************************************************** **/
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)

/** *****************************************************************************
 *                                                                              *
 *               SUPPLEMENTAL DATA SUPPORT ENABLEMENT                           *
 *                                                                              *
 ***************************************************************************** **/
// Default is false for ssb applications.
sdeEnabled=false

/** *****************************************************************************
 *                                                                              *
 *                     APPLICATION NAVIGATOR SUPPORT                            *
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
 *                AUTHENTICATION PROVIDER CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
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

/** *****************************************************************************
 *                                                                              *
 *                            CAS CONFIGURATION                                 *
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
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') +'/EmployeeSelfService/login/cas'
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/EmployeeSelfService/secure/receptor"
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
            logout {
                   afterLogoutUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL') ?: 'http://CAS_HOST:PORT/cas/logout?url=http://BANNER9_HOST:PORT/APP_NAME/')
                   mepErrorLogoutUrl = '/logout/logoutPage'
            }
        }
    }
}

/** *****************************************************************************
 *                                                                              *
 *                           SAML CONFIGURATION                                 *
 *          Un-comment the below code when authentication mode is saml.         *
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

/** ****************************************************************************
 *                                                                             *
 *                   HIBERNATE SECONDARY LEVEL CACHING                         *
 *                                                                             *
 **************************************************************************** **/
hibernate.cache.use_second_level_cache=true  // Default true. Make it false for Institution which is MEP enabled for HR or POSNCTL modules
hibernate.cache.use_query_cache=true         // Default true. Make it false for Institution which is MEP enabled for HR or POSNCTL modules

/** *****************************************************************************
 *                                                                              *
 *           HOME PAGE LINK WHEN ERROR HAPPENS DURING AUTHENTICATION.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://HOST:PORT/' )

/** *****************************************************************************
 *                                                                              *
 *                 ELIMINATE ACCESS TO THE WEB-INF FOLDER                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

/** *****************************************************************************
 *                                                                              *
 *                    WEB APPLICATION EXTENSIBILITY                             *
 *                                                                              *
 ***************************************************************************** **/
webAppExtensibility {
    locations {
       extensions = "path to the directory location where extensions JSON files will be written to and read from"
       resources = "path to the directory location where i18n files will be written to and read from"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}

/** ********************************************************************************
 *                                                                                 *
 *                   SS Config Dynamic Loading Job Properties                      *
 *                                                                                 *
 ******************************************************************************** **/
/* Set feature.enableConfigJob to true for configJob to run as configured and
set feature.enableConfigJob to false for configJob to NOT run as configured */

feature.enableConfigJob = true

/* Set feature.enableApplicationPageRoleJob to true for applicationPageRoleJob to run as configured and
set feature.enableApplicationPageRoleJob to false for applicationPageRoleJob to NOT run as configured */

feature.enableApplicationPageRoleJob = true

/** ********************************************************************************
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

/** *****************************************************************************
 *                                                                              *
 *                APPLICATION SERVER CONFIGURATION                              *
 *                                                                              *
 * When deployed to Tomcat, targetServer="tomcat"                               *
 * When deployed to WebLogic, targetServer="weblogic"                           *
 *                                                                              *
 ***************************************************************************** **/
targetServer="tomcat"

/** ******************************************************************************
 *                      ConfigJob (Platform 9.29)                                *
 * Used in BannerDS to wrap dbase calls in locale ( or not )                     *
 * Performance implications.  SS applications should set to true.                *
 * If loaded to GUROCFG - requires restart.                                      *
 *                                                                               *
 ****************************************************************************** **/
enableNLS=true

/** ************************************************************************************************************
 *                                       RESPONSE HEADERS                                                      *
 *                                                                                                             *
 * This is the map which takes the "header property" as key and value as shown in below example                *
 * responseHeaders = [ "X-Content-Type-Options": "nosniff" , "X-XSS-Protection": "1; mode=block" ...]          *
 * Added as part of Platform Platform 9.32                                                                     *
 ************************************************************************************************************ **/
responseHeaders = ["X-Content-Type-Options": "nosniff","X-XSS-Protection": "1; mode=block"]

/**************************************************************************************
* List of allowed domains configuration for Ellucian Experience                       *
* Do not change this configuration unless instructed.                                 *
* Do not move this configuration to Banner Applications Configurations (GUACONF) page.*
************************************************************************************ **/
allowedExperienceDomains=[
"https://experience-test.elluciancloud.com",
"https://experience.elluciancloud.com",
"https://experience-test.elluciancloud.ca",
"https://experience.elluciancloud.ca",
"https://experience-test.elluciancloud.ie",
"https://experience.elluciancloud.ie",
"https://experience-test.elluciancloud.com.au",
"https://experience.elluciancloud.com.au"]
