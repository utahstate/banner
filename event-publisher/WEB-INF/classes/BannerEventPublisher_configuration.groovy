/*******************************************************************************
Copyright 2017-2022 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

/** ****************************************************************************
*                                                                              *
*                Self-Service Banner 9 Event Configuration                     *
*                                                                              *
***************************************************************************** **/

/******************************************************************************

This file contains configuration needed by the Banner Event Publisher
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

* Self Service Support

* Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

* CAS SSO Configuration (supporting administrative and self service users)

NOTE: DataSource and JNDI configuration resides in the cross-module
'banner_configuration.example' file.

*******************************************************************************/

/*******************************************************************************
*                                                                              *
*              Banner Event Publisher DataSource Configuration                  *
*                                                                              *
*******************************************************************************/

dataSource_cdcadmin {
    // JNDI configuration for use in 'production' environment
    jndiName = "jdbc/cdcadmin"
    transactional = false
}

dataSource_events {
    // JNDI configuration for use in 'production' environment
    jndiName = "jdbc/events"
    transactional = false
}

bep {
	// Message Broker
	// Possible vaues are "RABBITMQ" or "WEBLOGIC" or "RABBITMQ/WEBLOGIC"
	message.broker = "RABBITMQ"

	//Retry interval to publish to broker in SECONDS
	publish.retry.interval = 45
}

// App Server
// Possible vaues are either "TOMCAT" or "WEBLOGIC"
bep.app.server = "TOMCAT"

//RabbitMQ configuration
rabbitmq {
	host = (System.getenv('RABBITMQ_HOST') ?: "rabbitmqHost")
	port = "5672"
	userName = (System.getenv('RABBITMQ_USERNAME') ?: "rabbitmqAdm")
	password = (System.getenv('RABBITMQ_PASSWORD') ?: "#UPDATEME#")
	virtualHostName = (System.getenv('RABBITMQ_VIRTUALHOSTNAME') ?: "bep_events_host")
	exchangeName = (System.getenv('RABBITMQ_EXCHANGENAME') ?:"bep_events_topic")
	enableSSL = (System.getenv('RABBITMQ_ENABLESSL') ?: "false")

	//Validate rabbit connections
    validate = true

	//Put an actual path to a file starting with "file:" otherwise leave the value as NO_FILE
	keyStoreFileName = "NO_FILE"
	keyStorePassPhrase = ""

	//Put an actual path to a file starting with "file:" otherwise leave the value as NO_FILE
	trustStoreFileName = "NO_FILE"
	trustStorePassPhrase = ""
}

jms {
	validate = true
}

// This configuration needs to be done in milliseconds for the footer to appear in the screen
footerFadeAwayTime=2000

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************

ssbEnabled = (System.getenv('SSBENABLED') ? Boolean.parseBoolean(System.getenv('SSBENABLED')) : true )
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.parseBoolean(System.getenv('SSBORACLEUSERSPROXIED')) : true )

/********************************************************************************
 *                                                                              *
 *                Banner Authentication Provider Configuration                  *
 *                                                                              *
 ********************************************************************************/

boolean ssoEnabled = false
banner {
	sso{
    authenticationProvider = 'default'
    authenticationAssertionAttribute = 'UDC_IDENTIFIER'
    if(authenticationProvider == 'cas' || authenticationProvider == 'saml'){
    ssoEnabled = true}}}
    if(ssoEnabled)
    {
    grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
    }

/********************************************************************************
 *                                                                              *
 *           Home Page URL configuration for CAS / SAML Single-Sign On          *
 *                                                                              *
 ********************************************************************************/
// Can be institutional home page ex: http://myportal/main_page.html
grails.plugin.springsecurity.homePageUrl='http://BANNER_EVENT_PUBLISHER_HOST:PORT/BannerEventPublisher/'


/********************************************************************************
 *                                                                              *
 *                             CAS SSO Configuration                            *
 *                                                                              *
 ********************************************************************************/
// Set active = true when Banner Event Publisher is configured for CAS SSO
grails {
    plugin {
        springsecurity {
            cas {
                active           = false
                serviceUrl       = 'http://BANNER_EVENT_PUBLISHER_HOST:PORT/BannerEventPublisher/login/cas'
                serverName       = 'http://BANNER_EVENT_PUBLISHER_HOST:PORT'
                proxyCallbackUrl = 'http://BANNER_EVENT_PUBLISHER_HOST:PORT/BannerEventPublisher/secure/receptor'
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
                    afterLogoutUrl    = 'https://CAS_HOST:PORT/cas/logout?url=http://BANNER_EVENT_PUBLISHER_HOST:PORT/BannerEventPublisher/'
                    mepErrorLogoutUrl = '/logout/logoutPage'
            }
        }
    }
}

// Cannot be declared bean style because the value is not available for reference in seamless.interceptPattern
grails.plugin.springsecurity.cas.serverUrlPrefix = "https://CAS_HOST:PORT/cas"


/********************************************************************************
 *                                                                              *
 *                         SAML2 SSO Configuration                              *
 *                                                                              *
 ********************************************************************************/
//Set active = true when Banner Event Publisher is configured for SAML2 SSO
/*grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'
banner.sso.authentication.saml.localLogout='false'	 // Setting localLogout to false, allows the application to send a single or global logout request to the Identity Service Provider
grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/samlkeystore.jks'     // for unix based 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'bep-sp': 'changeit' ]        // bep-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'bep-sp'                       // bep-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.metadata.sp.file = 'security/bep-sp.xml'               // for unix based '/home/u02/bep-sp.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security//bep-idp.xml']   // for unix based '/home/u02/bep-idp.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = '<entityID>'                     // Same value as configured in the IDP xml
grails.plugin.springsecurity.saml.maxAuthenticationAge = 14400  //value in seconds
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
    local: true,
    alias: 'bep-sp',                                                                 // bep-sp is the value set in EIS Service provider setup
    securityProfile: 'metaiop',
    signingKey: 'bep-sp',                                                            // bep-sp is the value set in EIS Service provider setup
    encryptionKey: 'bep-sp',                                                         // bep-sp is the value set in EIS Service provider setup
    tlsKey: 'bep-sp',                                                                // bep-sp is the value set in EIS Service provider setup
    requireArtifactResolveSigned: false,
    requireLogoutRequestSigned: false,
    requireLogoutResponseSigned: false
]*/

/** *****************************************************************************
 *                                                                              *
 *   This setting is needed if the application needs to work inside.            *
 *   Application Navigator and the secured application pages will be accessible *
 *   as part of the single-sign on solution.                                    *
 *   Added as part of Platform 9.17                                             *
 *                                                                              *
 *******************************************************************************/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true

/********************************************************************************
*                                                                               *
* AboutInfoAccessRoles will help to check whether user is allowed to view       *
* the platform version                                                          *
*                                                                               *
******************************************************************************* **/
aboutInfoAccessRoles = ["ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"]

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

/********************************************************************************
*                     Application Server Configuration                          *
* When deployed on Tomcat this configuration should be targetServer=“tomcat”    *
* When deployed on Weblogic this configuration should be targetServer=“weblogic”*
*********************************************************************************/
targetServer="tomcat"

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

