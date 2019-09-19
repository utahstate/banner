/*********************************************************************************
 Copyright 2015-2019 Ellucian Company L.P. and its affiliates.
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

logAppName = "CommunicationManagement"

// ******************************************************************************
//
//                       +++ JMX Bean Names +++
//
// ******************************************************************************

// The names used to register Mbeans must be unique for all applications deployed
// into the JVM.  The below configuration updates the name for each instance of
// Communication Management application to ensure uniqueness.
jmx {
    exported {
        java.util.Random rand = new java.util.Random()
        int max = 100
        def node = rand.nextInt(max+1)
        log4j = node+"-"+"communication-management-directory-log4j"

    }
}

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

/** ****************************************************************************
 *                                                                             *
 *              Commmgr User DataSource Configuration                          *
 *                                                                             *
 *******************************************************************************/
commmgrDataSourceEnabled = false  //Set this to true if using the bannerCommmgrDataSource

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
                afterLogoutUrl = '/'   // 'https://cas-server/logout?url=http://myportal/main_page.html'
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

// ******************************************************************************
//
// +++ Communication Management CONFIGURATION +++
//
// Note: Times such as pollingInterval and idleWaitTime are in milliseconds.
// ******************************************************************************

communication {
    weblogicDeployment = false

    communicationGroupSendMonitor {
        enabled = true
        monitorIntervalInSeconds = 10
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

    scheduler {
        enabled = true
        idleWaitTime = 30000
        clusterCheckinInterval = 15000
    }

    //These configuration values decide the display of the recurrent scheduling UI component.
    //Enabling seconds, minutes and hourly scheduling allows users to create communications that recur N seconds, minutes or hours respectively. 
    //This may cause a strain on your system.
    
    recurringScheduleOptions {
            enableMinutesScheduling = false
            enableHourlyScheduling = false
            enableDailyScheduling = true
            enableWeeklyScheduling = true
            enableMonthlyScheduling = true
            enableYearlyScheduling = false
            enableAdvancedOption = false
    }

    //Set to true if communications generated from the CommunicationManagement system should also be
    //tracked in the Banner Mail system (GURMAIL)

    bannerMailTrackingEnabled = false

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

/** ****************************************************************************
 *                                                                              *
 *              Web session timeout Configuration (in seconds)                  *
 *                                                                              *
 ***************************************************************************** **/
defaultWebSessionTimeout = 1500

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
/** ****************************************************************************
 *                                                                             *
 *              Access roles for viewing platform version in About dialog      *
 *                                                                             *
 *******************************************************************************/

aboutInfoAccessRoles = ['ROLE_SELFSERVICE_BAN_DEFAULT_M']

/** *****************************************************************************
 *                                                                              *
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl='http://URL:PORT/'


/**********************************************************************************
***    Google Analytics                                                           *
**********************************************************************************/
banner.analytics.trackerId=
banner.analytics.allowEllucianTracker=false


/**********************************************************************************
***    Theming - Configuration to use themes served by the Theme Server                      
*** banner.theme.url 
           Required only if theme server is remote   
           References the URL to the application hosting the Theme Server. 
           Example : http://<hostname>:<port>/CommunicationManagement/ssb/theme                    *
*** banner.theme.name 
           This is the desired theme name to use. In a MEP environment, the application uses the MEP code 
           as the theme name instead of the banner.theme.name. A theme by this name must be created in the Theme Editor
           on the server specified by banner.theme.url
*** banner.theme.template
           This is the name of the scss file containing the theme settings 
*** banner.theme.cacheTimeOut
           Time in seconds, required only if the app is theme server. The value indicates 
           how long the CSS file that was generated using the template and the theme is cached.
***********************************************************************************/
banner.theme.url = "<UPDATE_ME>"  
banner.theme.name = "<UPDATE_ME>"  
banner.theme.template = 'CommunicationManagement-9_6' 
banner.theme.cacheTimeOut = 120  

/** ******************************************************************************
 *                                                                               *
 *                      ConfigJob  (time in milliseconds                         *
 *                                                                               *
 *             Support for configurations to reside in the database.             *
 *                                                                               *
 * configJob.delay  //This is to configure when the quartz scheduler should start*
                      after the server startup, if its not configured then the   *
                      default value is 60000                                     *
 * configJob.interval //This is to configure the interval at which the quartz    *
                        scheduler should run, if its not configured then the     *
                        default value is 60000.                                  *
 * configJob.actualCount //Actual count will be the count how many times the     *
                           config job would run.                                 *
                          If the value is -1 then the job will run indefinitely. *
                          If the value is 0 job will not run.                    *
                          If not configured then the default value is -1.        *
 *                                                                               *
 *                                                                               *
 *                                                                               *
 ********************************************************************************/
configJob.delay = 60000
configJob.interval = 120000
configJob.actualCount = -1

/** *****************************************************************************
 *                                                                              *
 * Migrating the SeedData Keys Configuration and the security intercept urls
                                 to the database                                *
 *                                                                              *
 ***************************************************************************** **/
/* Here are 3 patterns to use to configure the SeedData keys

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

**************************************************************************************/

ssconfig.app.seeddata.keys = [

    ['grails.plugin.springsecurity.interceptUrlMap'],

    ['ssbEnabled'],
    ['ssbOracleUsersProxied'],
    ['ssbPassword.reset.enabled'],

    ['communication.recurringScheduleOptions.enableMinutesScheduling'],
    ['communication.recurringScheduleOptions.enableHourlyScheduling'],
    ['communication.recurringScheduleOptions.enableDailyScheduling'],
    ['communication.recurringScheduleOptions.enableWeeklyScheduling'],
    ['communication.recurringScheduleOptions.enableMonthlyScheduling'],
    ['communication.recurringScheduleOptions.enableYearlyScheduling'],
    ['communication.recurringScheduleOptions.enableAdvancedOption'],
    
    ['defaultWebSessionTimeout'],
    ['aboutInfoAccessRoles'],

    ['banner.theme.url'],
    ['banner.theme.name'],
    ['banner.theme.template'],
    ['banner.theme.cacheTimeOut'],

    ['banner.analytics.trackerId'],
    ['banner.analytics.allowEllucianTracker']
]

/*********************************************************************************
*                     Application Server Configuration                           *
* When deployed on Tomcat this configuration should be targetServer="tomcat"     *
* When deployed on Weblogic this configuration should be targetServer="weblogic" *
**********************************************************************************/
targetServer="tomcat"

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

