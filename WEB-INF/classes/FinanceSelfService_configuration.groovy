/** *****************************************************************************
 Copyright 2014-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

/** ****************************************************************************
 *                                                                              *
 *          Banner 9 Finance Self-Service Configuration                         *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

 This file contains configuration needed by the Self-Service Banner 9 Finance
 web application. Please refer to the administration guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:

 * Self Service Support
 * CAS SSO Configuration (supporting administrative and self service users)
 * SAML SSO Configuration (supporting administrative and self service users)
 * Web Application Extensibility
 * Config Job Configuration
 * Target Server Configuration
 * enableNLS Configuration

 NOTE: DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.
 ***************************************************************************** **/

/** *****************************************************************************
 *                                                                              *
 *                         Self Service Support                                 *
 *                                                                              *
 ***************************************************************************** **/
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.parseBoolean(System.getenv('SSBORACLEUSERSPROXIED')) : true)

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
 *                BANNER AUTHENTICATION PROVIDER CONFIGURATION                	*
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default, cas or saml
// If you are using cas then the CAS CONFIGURATION also need to be configured/uncommented as well as set to active.
// If you are using saml then the SAML CONFIGURATION also need to be configured/uncommented as well as set to active.
//
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
 *                             CAS CONFIGURATION                                *
 *                                                                              *
 ***************************************************************************** **/

// set active = true when authentication provider section configured for cas
grails {
    plugin {
        springsecurity {
            cas {
                active = true
                serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/FinanceSelfService/login/cas"
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/FinanceSelfService/secure/receptor"
                loginUri = '/login'
                sendRenew = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                serverUrlEncoding = 'UTF-8'
                filterProcessesUrl = '/login/cas'
                if (useSingleSignout) {
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                afterLogoutUrl = (System.getenv('BANNER9_AFTERLOGOUTURL') ?:  'https://cas-server/logout?url=http://myportal/main_page.html' )
            }
        }
    }
}

/** *************************************************************************************
 *                                                                                      *
 *                        SAML CONFIGURATION                                            *
 *  Un-comment the below code and set active = true when authentication mode is saml.   *
 *                                                                                      *
 ************************************************************************************* **/
/*
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'

banner.sso.authentication.saml.localLogout='false' // To disable single logout set this to true,default 'false'.

grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/<KEY_NAME>.jks'  // for unix File based Example:- 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-<short-appName>-sp': 'changeit' ]  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-<short-appName>-sp'                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup

grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-<Application_Name>-sp.xml'            // for unix file based Example:-'/home/u02/sp-local.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security/banner-<Application_Name>-idp.xml'] // for unix file based Example: '/home/u02/idp-local.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
        local: true,
        alias: 'banner-<short-appName>-sp',                  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        securityProfile: 'metaiop',
        signingKey: 'banner-<short-appName>-sp',             // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        encryptionKey: 'banner-<short-appName>-sp',          // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        tlsKey: 'banner-<short-appName>-sp',                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        requireArtifactResolveSigned: false,
        requireLogoutRequestSigned: false,
        requireLogoutResponseSigned: false
]*/

/** ***************************************************************************
 *               Web Application Extensibility                                  *
 *******************************************************************************/

webAppExtensibility {
    locations {
        extensions = 'C:/BanXE/Extensions/ss_ext/extensions/'
        // for unix based Example:-'/home/oracle/config_extn/ssb/extensions/'
        resources = 'C:/BanXE/Extensions/ss_ext/i18n/'
        // for unix based Example:-'/home/oracle/config_extn/ssb/i18n/'
    }
    adminRoles = 'ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'
}

// URL of application home page Example: http://localhost:port/FinanceSelfService/
grails.plugin.springsecurity.homePageUrl =  (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://localhost:port/FinanceSelfService/' )



/** *****************************************************************************
 *                                                                              *
 *                      ConfigJob (Platform 9.23)                               *
 *     Support for configurations to reside in the database.                    *
 *                                                                              *
 ***************************************************************************** **/
configJob.interval = 120000
configJob.actualCount = -1


/*********************************************************************************
*                     Application Server Configuration                          *
* When deployed on Tomcat this configuration should be targetServer="tomcat"    *
* When deployed on Weblogic this configuration should be targetServer="weblogic"*
****************************************************************************** **/
targetServer="tomcat"



/** *******************************************************************************
 *                      enableNLS (Platform 9.29.1)                               *
 * Setting it to true will set National Language support in the Oracle database   *
 * to the user specific language, that is the error messages from Oracle database *
 * will be in the user specific language, while setting it to false would disable *
 * the Nation Language support for the error messages from Oracle database and    *
 * improves the performance of the application.                                   *
 ******************************************************************************* **/
enableNLS = true