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
banner {
    sso {
		authenticationProvider = 'cas'
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
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

/** *****************************************************************************
 *                                                                              *
 *                           SAML CONFIGURATION                                 *
 *          Un-comment the below code when authentication mode is saml.         *
 *                                                                              *
 ***************************************************************************** **/
// set active = true when authentication provider section configured for saml
grails.plugin.springsecurity.saml.active = false
/* grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
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