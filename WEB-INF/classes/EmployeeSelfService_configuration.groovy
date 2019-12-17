/*********************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/

/** ****************************************************************************
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

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

    * CAS SSO Configuration (supporting administrative and self service users)

     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.

***************************************************************************** **/

/***********************************************************************************
                     Application Server Configuration
 When deployed on Tomcat this configuration should be targetServer="tomcat"
 When deployed on Weblogic this configuration should be targetServer="weblogic"
*********************************************************************************/
targetServer="tomcat"

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED') ? Boolean.parseBoolean(System.getenv('SSBPASSWORD_RESET_ENABLED')) : true) //true  - allow Pidm users to reset their password.
                                  //false - throws functionality disabled error message

// *****************************************************************************
//
//                     +++ iframe Denial Setting +++
//
// *****************************************************************************
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


// ******************************************************************************
//
//                       +++ CAS CONFIGURATION +++
//
// ******************************************************************************

banner {
    sso {
		authenticationProvider = 'cas'
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        }
}
if (banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' ) {
   grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
}

grails {
    plugin {
        springsecurity {
            cas {
                active = true
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

/** *************************************************************************************
 *                                                                              		*
 *                        SAML CONFIGURATION                                    		*
 *  Un-comment the below code and set active = true when authentication mode is saml.   *
 *                                                                              		*
 ************************************************************************************* **/
/*
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'

banner.sso.authentication.saml.localLogout='false'	// To disable single logout set this to true,default 'false'.

grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/<KEY_NAME>.jks'	// for unix File based Example:- 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-<short-appName>-sp': 'changeit' ]  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-<short-appName>-sp'                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup

grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-<Application_Name>-sp.xml'            // for unix file based Example:-'/home/u02/sp-local.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security/banner-<Application_Name>-idp.xml'] // for unix file based Example: '/home/u02/idp-local.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
        local: true,
        alias: 'banner-<short-appName>-sp',     		     // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        securityProfile: 'metaiop',
        signingKey: 'banner-<short-appName>-sp',             // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        encryptionKey: 'banner-<short-appName>-sp',          // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        tlsKey: 'banner-<short-appName>-sp',                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        requireArtifactResolveSigned: false,
        requireLogoutRequestSigned: false,
        requireLogoutResponseSigned: false
]*/


/** *****************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']




/** ***************************************************************************
 *               Web Application Extensibility                                  *
 *******************************************************************************/

webAppExtensibility {
    locations {
       extensions = "path to the directory location where extensions JSON files will be written to and read from"
       resources = "path to the directory location where i18n files will be written to and read from"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}

/******************************************************************************
 *                   Hibernate Secondary Level Caching                         *
 *******************************************************************************/
hibernate.cache.use_second_level_cache=true  // Default true. Make it false for Institution which is MEP enabled for HR or POSNCTL modules
hibernate.cache.use_query_cache=true         // Default true. Make it false for Institution which is MEP enabled for HR or POSNCTL modules



/** *****************************************************************************
 *                                                                              *
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://HOST:PORT/' )


/** *************************************************************************************
*                                                                                       *
*                    ++++Quartz scheduler Configurations ++++                           *
************************************************************************************** **/
configJob.delay=60000   //Time in milliseconds to configure when the quartz scheduler should start after the server startup, if its not configured then the default value is 60000.
configJob.interval=60000 //Time in milliseconds to configure the interval at which the quartz schedule should run, default value is 60000 if not configured.
configJob.actualCount=-1 //The count of number of times the config job would run.  If value is -1, the job will run indefinitely.  If the value is o, the job will not run.  Default value is -1 when not configured.


/* Here are 3 patterns to use to configure the keys

Pattern 1 - With key and value
Syntax:
ssconfig.app.seeddata.keys = [
['<Key1>': <Boolean value>], ['<Key2>': <Boolean Value2>], ['<Key3>': '<String Value>'], ['<Key4>':<Numeric value>]
]

Pattern 2 - With key only (value derived from configuration files)
Syntax:
ssconfig.app.seeddata.keys = [
['<Key1>'], ['<Key2>'], ['<Key3>'], ['<Key4>']
]

Pattern 3 - Combination of Pattern 1 and 2 - With key only and key/value pairs.
Syntax:
ssconfig.app.seeddata.keys = [
['<Key1>': <Boolean value>], ['<Key2>'], ['<Key3>': '<String Value>'], ['<Key4>']
]

*/

//Down below are keys configured using pattern 2
ssconfig.app.seeddata.keys = [
['grails.plugin.springsecurity.interceptUrlMap'],
]
