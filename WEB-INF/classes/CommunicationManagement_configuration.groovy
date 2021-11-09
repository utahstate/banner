/*********************************************************************************
 Copyright 2015-2020 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/
 
 /** ****************************************************************************
 *                                                                              *
 *          Self-Service Banner Communication Management Configuration          *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

This file contains configuration needed by the Self-Service Banner Communication Management
web application. Please refer to the administration guide for 
additional information regarding the configuration items contained within this file. 

This configuration file contains the following sections: 
    
    * Self Service Support
    
    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart) 
         
    * CAS SSO Configuration (supporting administrative and self service users)
    
     NOTE: DataSource and JNDI configuration resides in the cross-module 
           'banner_configuration.groovy' file. 
    
***************************************************************************** **/

footerFadeAwayTime = 0

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************


ssbEnabled = true
ssbOracleUsersProxied = true
ssbPassword.reset.enabled = true //true  - allow Pidm users to reset their password.
                                 //false - throws functionality disabled error message
enableNLS=true

// ******************************************************************************
//
//                       API Configuration
// In CAS and SAML modes, set the below attributes to true to allow API calls to bypass single sign-on authentication
//
// ******************************************************************************
guestAuthenticationEnabled = false
apiOracleUsersProxied = false


/** ****************************************************************************
 *                                                                             *
 *              Commmgr User DataSource Configuration                          *
 *                                                                             *
 *******************************************************************************/
commmgrDataSourceEnabled = true  //Set this to true if using the bannerCommmgrDataSource

/** *****************************************************************************
 *                                                                              *
 *                AUTHENTICATION PROVIDER CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default or cas 
banner {
    sso {
		authenticationProvider = 'cas'
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
                serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/CommunicationManagement/login/cas'
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') 
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/CommunicationManagement/secure/receptor'
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/login/cas'
                serverUrlEncoding = 'UTF-8'
                if (active && useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
		    logout {
                afterLogoutUrl = (System.getenv('BANNER9_AFTERLOGOUTURL') ?: 'https://cas-server/logout?url=http://myportal/main_page.html')
                mepErrorLogoutUrl = 'https://URL:PORT/'
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
grails.plugin.springsecurity.saml.active = false

/*grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'
banner.sso.authentication.saml.localLogout='false'                                                    // To disable single logout set this to true,default 'false'.

grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/<KEY_NAME>.jks'          // for unix File based Example:- 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'test1234'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-<short-appName>-sp': 'test1234' ]  // banner-<short-appName>-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-<short-appName>-sp'                 // banner-<short-appName>-sp is the value set in EIS Service provider setup

grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-<Application_Name>-sp.xml'     // for unix file based Example:-'/home/u02/sp-local.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security/banner-<Application_Name>-idp.xml'] // for unix file based Example: '/home/u02/idp-local.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
        local: true,
        alias: 'banner-<short-appName>-sp',                                   // banner-<short-appName>-sp is the value set in EIS Service provider setup
        securityProfile: 'metaiop',
        signingKey: 'banner-<short-appName>-sp',                              // banner-<short-appName>-sp is the value set in EIS Service provider setup
        encryptionKey: 'banner-<short-appName>-sp',                           // banner-<short-appName>-sp is the value set in EIS Service provider setup
        tlsKey: 'banner-<short-appName>-sp',                                  // banner-<short-appName>-sp is the value set in EIS Service provider setup
        requireArtifactResolveSigned: false,
        requireLogoutRequestSigned: false,
        requireLogoutResponseSigned: false
]
*/

// ************************************************************************************************************
//
//  +++ Protecting against cross-frame scripting vulnerability when integrating with Application Navigator +++
//
// ************************************************************************************************************             
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true

/*********************************************************************************
*                     Application Server Configuration                           *
* When deployed on Tomcat this configuration should be targetServer="tomcat"     *
* When deployed on Weblogic this configuration should be targetServer="weblogic" *
**********************************************************************************/
targetServer="tomcat"

// ******************************************************************************
//
// +++ Communication Management CONFIGURATION +++
//
// Note: Times such as pollingInterval and idleWaitTime are in milliseconds.
// ******************************************************************************

communication {
	weblogicDeployment = targetServer=="weblogic" ? true: false
	println("weblogicDeployment is "+ weblogicDeployment);

    communicationGroupSendMonitor {
        enabled = true
        monitorIntervalInSeconds = 10
    }

    communicationItemMonitor {
        enabled = false
        monitorIntervalInSeconds = 30
    }

    communicationGroupSendItemProcessingEngine {
        enabled = true
        maxThreads = 1
        maxQueueSize = 5000
        continuousPolling = true
        pollingInterval = 2000
        deleteSuccessfullyCompleted = false
    }

    communicationJobProcessingEngine {
        enabled = true
        maxThreads = 2
        maxQueueSize = 5000
        continuousPolling = true
        pollingInterval = 2000
        deleteSuccessfullyCompleted = false
    }
    communicationItemProcessingEngine {
        enabled = false
        maxThreads = 2
        maxQueueSize = 5000
        continuousPolling = true
        pollingInterval = 2000
        deleteSuccessfullyCompleted = false
    }
    scheduler {
        enabled = true
        idleWaitTime = 30000
        clusterCheckinInterval = 15000
    }

    //Set to true if communications generated from the CommunicationManagement system should also be
    //tracked in the Banner Mail system (GURMAIL)

    bannerMailTrackingEnabled = true

    //Set to true if using the Banner Bank automated clearance system (BACS)
    //Default is false

    bacsEnabled = false

}

responseHeaders =[
   "X-Content-Type-Options": "nosniff",
   "X-XSS-Protection": "1; mode=block"
]

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

/** *****************************************************************************
 *                                                                              *
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: '<HOME_URL>' )



/** ********************************************************************************
 *                                                                                 *
 *                   SS Config Dynamic Loading Job Properties                      *
 *                                                                                 *
 * Properties to set the interval and the number of times the config job would run *
 * for ConfigJob.groovy i.e. the job scheduled to update the configuration 		   *
 * properties from DB. We recommend configuring interval of the configJob in 	   *
 * such a way that it does not run as often, to help improve performance.          *
 *                                                                                 *
 * interval - in milliseonds, this is to configure the interval at which the       *
 * quartz scheduler should run. If it is not configured, the default value is 60000*
 *                                                                                 *
 * actualCount - the number of times the config job would run. If the value is -1, *
 * the job will run indefinitely. If the value is 0, the job will not run.         *
 * If not configured, the default value is -1                                      *
 *   																			   *
 ******************************************************************************** **/

configJob {
    interval = 120000
    actualCount = -1
}

// ******************************************************************************
//
//                       +++ QUARTZ CONFIGURATION +++
//                               DO NOT UPDATE
// ******************************************************************************
quartz {

     println "Reading Quartz Scheduler properties from external configuration!"
     
     autoStartup = communication.scheduler.enabled ==true ? true: false
	 jdbcStore =  false
	 waitForJobsToCompleteOnShutdown=true
	 purgeQuartzTablesOnStartup=false
	 pluginEnabled=true

       scheduler.skipUpdateCheck = true
       scheduler.instanceName = 'Banner Quartz Scheduler'
       scheduler.instanceId = 'BCM'

       if (communication.scheduler.idleWaitTime) {
           scheduler.idleWaitTime =communication.scheduler.idleWaitTime
       }

       boolean isWebLogic = communication.weblogicDeployment == true
       if (isWebLogic) {
           println( "Setting driverDelegateClass to org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate" )
           jobStore.driverDelegateClass = 'org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate'
       } else {
           println( "Setting driverDelegateClass to org.quartz.impl.jdbcjobstore.oracle.OracleDelegate" )
           jobStore.driverDelegateClass = 'org.quartz.impl.jdbcjobstore.oracle.OracleDelegate'
       }
       jobStore.class = 'net.hedtech.banner.general.scheduler.quartz.BannerDataSourceJobStoreCMT'

       jobStore.tablePrefix = 'GCRQRTZ_' // Share tables. communication has own instance
       jobStore.isClustered = true
       if (communication.scheduler.clusterCheckinInterval) {
           jobStore.clusterCheckinInterval = communication.scheduler.clusterCheckinInterval
       }
       jobStore.useProperties = false

    println "Completed reading Quartz Scheduler properties from external configuration!"
}

