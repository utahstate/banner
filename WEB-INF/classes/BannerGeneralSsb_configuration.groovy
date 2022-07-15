/*********************************************************************************
 Copyright 2015-2021 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/

/******************************************************************************
 *                                                                            *
 *          Self-Service Banner 9 General Self-Service Configuration          *
 *                                                                            *
 ******************************************************************************/

/******************************************************************************

 This file contains configuration needed by the Banner 9 General Self-Service web
 application. Please refer to the General Self-Service configuration documentation
 for more information regarding the configuration items contained within this file.

 This configuration file contains the following sections:

 * Self Service Support
 * Commmgr User DataSource Configuration
 * Supplemental Data Support Enablement
 * Application name to refer to this app for name display rules
 * Authentication Provider Configuration
 * CAS Configuration - SSO (supporting administrative and self service users)
 * SAML Configuration - SSO (supporting administrative and self service users)
 * Security HTTP Response Header Configuration
 * Application Server Configuration
 * Extensibility Extensions & i18n File Location
 * Home Page URL Configuration
 * Eliminate Access to the WEB-INF Folder
 * Page Builder Artifact File Location Configuration
 * Configuration for AIP application
 * ClamAV Antivirus Scanner Configurations
 * Config Job Configurations
 * Migrating the SeedData Keys Configuration to the Database
 * Action Item processing Configurations
 * Action Item Processing Quartz Configurations

 *******************************************************************************/


/*******************************************************************************
 *                                                                             *
 *                         Self Service Support                                *
 *                                                                             *
 *******************************************************************************/
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.parseBoolean(System.getenv('SSBORACLEUSERSPROXIED')) : true)
guestAuthenticationEnabled = (System.getenv('GUEST_AUTH_ENABLED') ? Boolean.parseBoolean(System.getenv('GUEST_AUTH_ENABLED')): true )//Set to true if enabling the Proxy Access


/******************************************************************************
 *                                                                            *
 *              Commmgr User DataSource Configuration                         *
 *                                                                            *
 ******************************************************************************/
commmgrDataSourceEnabled = (System.getenv('COMMMGRDATASOURCEENABLED') ? Boolean.parseBoolean(System.getenv('COMMMGRDATASOURCEENABLED')) : false )  //Set this to true if using the bannerCommmgrDataSource


/*******************************************************************************
 *                                                                             *
 *  This setting is needed if the application needs to work inside             *
 *  Application Navigator and the secured application pages will be accessible *
 *  as part of the single-sign on solution.                                    *
 *                                                                             *
 *******************************************************************************/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


/*******************************************************************************
 *                                                                             *
 *               Supplemental Data Support Enablement                          *
 *                                                                             *
 *******************************************************************************/
// Default is false for self-service applications.
sdeEnabled = false


/*******************************************************************************
 *                                                                             *
 *                Authentication Provider Configuration                        *
 *                                                                             *
 *******************************************************************************/
//
// Set authenticationProvider to either default or cas.
// If using cas, the CAS Configuration will also need to be
// configured/uncommented as well as set to active.
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


/*******************************************************************************
 *                                                                             *
 *                             CAS Configuration                               *
 *                                                                             *
 *******************************************************************************/
// set active = true when authentication provider section configured for cas
grails {
    plugin {
        springsecurity {
            cas {
                if(System.getenv('AUTH_METHOD') == 'cas') { active = true }
                if(System.getenv('AUTH_METHOD') == 'saml') { active = false }
                    serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                    serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http:/BANNER9_HOST:PORT') + '/BannerGeneralSsb/login/cas'
                    serverName       = (System.getenv('BANNER9_URL') ?: 'http:/BANNER9_HOST:PORT')
                    proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http:/BANNER9_HOST:PORT') + '/BannerGeneralSsb/secure/receptor'
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/login/cas'
                serverUrlEncoding = 'UTF-8'
                if (active && useSingleSignout) {
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


/*******************************************************************************
 *                                                                             *
 *                        SAML Configuration                                   *
 *        Un-comment the below code when authentication mode is saml.          *
 *                                                                             *
 *******************************************************************************/
// Set active = true when authentication provider section configured for SAML

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



/****************************************************************************
 *                                                                          *
 *              Security HTTP Response Header Configuration                 *
 *                                                                          *
 ****************************************************************************/
responseHeaders =[
   "X-Content-Type-Options": "nosniff",
   "X-XSS-Protection": "1; mode=block"
]


/**********************************************************************************
 *                                                                                *
 *                     Application Server Configuration                           *
 * When deployed on Tomcat this configuration should be targetServer="tomcat"     *
 * When deployed on Weblogic this configuration should be targetServer="weblogic" *
 *                                                                                *
 **********************************************************************************/
targetServer="tomcat"


/*******************************************************************************
 *                                                                             *
 *              Extensibility Extensions & i18n File Location                  *
 *                                                                             *
 *******************************************************************************/
webAppExtensibility {
    locations {
        extensions = "path to the directory location where extensions JSON files will be written to and read from"
        resources = "path to the directory location where i18n files will be written to and read from"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}


/******************************************************************************
 *                                                                            *
 *                        Home Page URL Configuration                         *
 *          Home page link when error happens during authentication.          *
 *                                                                            *
 ******************************************************************************/
grails.plugin.springsecurity.homePageUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://BANNER9_HOME:PORT/StudentRegistrationSsb' )


/*******************************************************************************
 *                                                                             *
 *                 Eliminate Access to the WEB-INF Folder                      *
 *                                                                             *
 *******************************************************************************/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']


/*******************************************************************************
 *                                                                             *
 *              Page Builder Artifact File Location Configuration              *
 *                                                                             *
 *******************************************************************************/
pageBuilder.enabled = (System.getenv('PAGEBUILDER_ENABLED') ? Boolean.parseBoolean(System.getenv('PAGEBUILDER_ENABLED')): true)

// Initial load location on file system. Files located in "pb" directory.
// pb directory located at root of app as sibling to the config files.
pbRoot = (System.getenv('PBROOT') ?: '/opt/banner/pb') // Example /temp/pb
pageBuilder {
    locations {
      bundle        = "${pbRoot}/i18n"
      page          = "${pbRoot}/page"
      css           = "${pbRoot}/css"
      virtualDomain = "${pbRoot}/virtdom"
    }
    // Uncomment debugRoles to reveal detailed SQL error messages for
    // virtual domains to users with any of the comma separated roles.
    // debugRoles = "ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M"
}


/*************************************************************************
 *                                                                       *
 *                 Configuration for AIP application                     *
 *                                                                       *
 *************************************************************************/
BANNER_AIP_EXCLUDE_LIST='aipActionItemPosting|aipAdmin|aip|aipPageBuilder|BCM|about|cssManager|cssRender|error|excelExportBase|dateConverter|keepAlive|login|logout|resetPassword|securityQa|selfServiceMenu|survey|test|theme|themeEditor|userAgreement|userPreference'// No change in this.
// in case of new controller which needs to be ignored, can be added here.



/*******************************************************************************
 *                                                                             *
 *                  ClamAV Antivirus Scanner Configurations                    *
 *                                                                             *
 *******************************************************************************/
   clamav.enabled = false

   clamav.host = '<CLAMD_HOST_IP>' 	  /*Host IP address where the ClamAV daemon ( clamd ) is running. Default is '127.0.0.1' */
   clamav.port = '<CLAMD_PORT>'		  /* Port on which clamd process is listening. Default is 3310*/
   clamav.connectionTimeout = 5000 	  /* Connection timeout to connect to clamd process.Time in milliseconds. Default is 5000*/


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
    cronExpression = "0 0 */1 * * ?"
}
applicationPageRoleJob {
    // Recommended default is once at 00:00:00am every day - "0 0 0 * * ?"
    // Cron expression lesser than 30 mins will fall back to 30 mins.
    cronExpression = "0 0 0 * * ?"
}

/*******************************************************************************
 *                                                                             *
 *                  Action Item processing Configurations                      *
 *                                                                             *
 *******************************************************************************/
aip {
    weblogicDeployment = false

    actionItemPostMonitor {
        enabled = true
        monitorIntervalInSeconds = 10
    }

    actionItemPostWorkProcessingEngine {
        enabled = true
        maxThreads = 1
        maxQueueSize = 5000
        continuousPolling = true
        pollingInterval = 2000
        deleteSuccessfullyCompleted = false
    }

    actionItemJobProcessingEngine {
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
    }
}


/*******************************************************************************
 *                                                                             *
 *                  Action Item processing and  Quartz Configurations          *
 *                                                                             *
 *******************************************************************************/
general.aip.enabled = false

if(general.aip.enabled){
	aip {
        actionItemPostMonitor {
            enabled = true
            monitorIntervalInSeconds = 10
        }

        actionItemPostWorkProcessingEngine {
            enabled = true
            maxThreads = 1
            maxQueueSize = 5000
            continuousPolling = true
            pollingInterval = 2000
            deleteSuccessfullyCompleted = false
        }

        actionItemJobProcessingEngine {
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

	}
	quartz {
        autoStartup = true
        jdbcStore =  false
        scheduler.skipUpdateCheck = true
        scheduler.instanceName = 'Action Item Quartz Scheduler'
        scheduler.instanceId = 'AIP'
        waitForJobsToCompleteOnShutdown=true
        purgeQuartzTablesOnStartup=false
        pluginEnabled=true

        if (aip.scheduler.idleWaitTime) {
           scheduler.idleWaitTime =aip.scheduler.idleWaitTime
        }

        boolean isWebLogic = targetServer == 'weblogic'?true:false
        if (isWebLogic) {
           println( "Setting driverDelegateClass to org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate" )
           jobStore.driverDelegateClass = 'org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate'
        } else {
           println( "Setting driverDelegateClass to org.quartz.impl.jdbcjobstore.oracle.OracleDelegate" )
           jobStore.driverDelegateClass = 'org.quartz.impl.jdbcjobstore.oracle.OracleDelegate'
        }
        jobStore.class = 'net.hedtech.banner.general.scheduler.quartz.BannerDataSourceJobStoreCMT'

        jobStore.tablePrefix = 'GCRQRTZ_' // Share tables. AIP has own instance
        jobStore.isClustered = true
        if (aip.scheduler.clusterCheckinInterval) {
           jobStore.clusterCheckinInterval = aip.scheduler.clusterCheckinInterval
        }
        jobStore.useProperties = false

        println "Quartz Scheduler properties are initialized!"
	}
}
/** *******************************************************************************
 *                      enableNLS (Platform 9.29.1)                               *
 * Setting it to true will set National Language support in the Oracle database   *
 * to the user specific language, that is the error messages from Oracle database *
 * will be in the user specific language, while setting it to false would disable *
 * the Nation Language support for the error messages from Oracle database and    *
 * improves the performance of the application.                                   *
 ******************************************************************************* **/
enableNLS = true

