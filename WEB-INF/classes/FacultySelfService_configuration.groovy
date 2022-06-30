/**************************************************************************************
    Copyright 2018-2022 Ellucian Company L.P. and its affiliates.
 **************************************************************************************/

/** ***********************************************************************************
 *                                                                                    *
 *            Self-Service Banner 9 Faculty Self-Service Configuration                *
 *                                                                                    *
 **************************************************************************************/

/** ***********************************************************************************

This file contains configuration needed by the Self-Service Banner 9 Faculty Grade Entry
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * Self Service Support
    * CAS SSO Configuration (supporting administrative and self service users)    
    * Authentication Provider Configuration
    * Cas Configuration   
    * Extensibility Extensions & I18n File Location
    * Saml Configuration
    * Home Page Link
    * Configjob

    NOTE: DataSource and JNDI configuration resides in the cross-module
    'banner_configuration.groovy' file.

*********************************************************************************** **/

/** ***********************************************************************************
 *                                                                                    *
 *                              Self Service Support                                  *
 *                                                                                    *
 *********************************************************************************** **/
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)



/** ***********************************************************************************
 *                                                                                    *
 *                AUTHENTICATION PROVIDER CONFIGURATION                               *
 *                                                                                    *
 *********************************************************************************** **/
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

if(banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' )
{
    grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
}

/** ***********************************************************************************
 *                                                                                    *
 *                             CAS CONFIGURATION                                      *
 *                                                                                    *
 *********************************************************************************** **/
grails {
    plugin {
        springsecurity {
            cas {
                if(System.getenv('AUTH_METHOD') == 'cas') { active = true }
                if(System.getenv('AUTH_METHOD') == 'saml') { active = false }
                  serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                  serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/FacultySelfService/login/cas'
                  serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') 
                  proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/FacultySelfService/secure/receptor'
                  loginUri         = '/login'
                  sendRenew        = false
                  proxyReceptorUrl = '/secure/receptor'
                  useSingleSignout = true
                  key = 'grails-spring-security-cas'
                  artifactParameter = 'SAMLart'
                  serviceParameter = 'TARGET'
                  serverUrlEncoding = 'UTF-8'
                  filterProcessesUrl = '/login/cas'
                  if ( active && useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                  }
            }
            logout {
                  afterLogoutUrl    = (System.getenv('BANNER9_AFTERLOGOUTURL') ?:  'https://cas-server/logout?url=http://myportal/main_page.html' )
                  mepErrorLogoutUrl = '/logout/logoutPage'
            }
        }
    }
}

//Access the student profile page through clicking on student name hyperlink or contact card view profile button click.
//To ensure that the Banner Student Faculty Grade Entry module can communicate with the Banner Student Student Profile module,
// modify the bannerXE.url.mapper.studentProfile parameter to point to the full path to the Banner Student Student Profile module



/** ***********************************************************************************
 *                                                                                    *
 *                      Extensibility extensions & i18n file location                 *
 *                                                                                    *
 *********************************************************************************** **/
 webAppExtensibility {
    locations {
       extensions = "/home/oracle/config_extn/ssb/extensions"
       resources  = "/home/oracle/config_extn/ssb/i18n"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
 }   

/** ***********************************************************************************
 *                                                                                    *
 *                        SAML CONFIGURATION                                          *
 *        Un-comment the below code when authentication mode is saml.                 *
 *                                                                                    *
 *********************************************************************************** **/
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


/** ***********************************************************************************
 *                                                                                    *
 *           Home Page link when error happens during authentication.                 *
 *                                                                                    *
 *********************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: '<HOME_URL>' )


/** ***********************************************************************************
 *                                                                                    *
 *   This setting is needed if the application needs to work inside                   *
 *   Application Navigator and the secured application pages will be accessible       *
 *   as part of the single-sign on solution.                                          *
 *                                                                                    *
 *********************************************************************************** **/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


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
    interval = 120000
    actualCount = -1
    cronExpression = "0 0 */1 * * ?"
}
applicationPageRoleJob {
    // Recommended default is once at 00:00:00am every day - "0 0 0 * * ?"
    // Cron expression lesser than 30 mins will fall back to 30 mins.
    cronExpression = "0 0 0 * * ?"
}

/********************************************************************************
*                                                                               *
*                           Target Server                                       *
********************************************************************************/
/** *****************************************************************************
 *                                                                              *
 *                Application Server Configuration                              *
 * When deployed to Tomcat, targetServer="tomcat"                               *
 * When deployed to WebLogic, targetServer="weblogic"                           *
 *                                                                              *
 ***************************************************************************** **/
targetServer="tomcat"

/********************************************************************************
*                                                                               *
*                      ConfigJob (Platform 9.29)                                *
* Used in BannerDS to wrap dbase calls in locale ( or not )                     *
* Performance implications.  SS applications should set to true.                *
* If loaded to GUROCFG - requires restart.
*                                                                               *
******************************************************************************* **/
enableNLS=true
banner.applicationName="Faculty Self Service"
