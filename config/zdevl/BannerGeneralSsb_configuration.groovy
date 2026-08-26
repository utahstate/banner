/*********************************************************************************
 Copyright 2015-2024 Ellucian Company L.P. and its affiliates.
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




/***********************************************************************************
  Set 'isExperienceIntegrated' to true for accessing the SSB application only in
  Experience. Set to false to access the SSB application in standalone mode.
  Default value is 'false'
************************************************************************************/
isExperienceIntegrated = false

/*******************************************************************************
 *                                                                             *
 *               Supplemental Data Support Enablement                          *
 *                                                                             *
 *******************************************************************************/
// Default is false for self-service applications.
sdeEnabled = false


/*******************************************************************************
                                                                               *
                   OAuth2 configuration                                        *
                                                                               *
********************************************************************************/
banner.oauth2.issuerJwksURi= "https://oauth.prod.10005.elluciancloud.com/jwks"
banner.oauth2.issuer = "https://oauth.prod.10005.elluciancloud.com"
banner.oauth2.audiance="https://elluciancloud.com"
/*******************************************************************************
 *                                                                             *
 *                Authentication Provider Configuration                        *
 *                                                                             *
 *******************************************************************************/

banner {
    sso {
        authenticationProvider           = 'saml' //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
    }
}

/*******************************************************************************
 *                                                                             *
 *                        SAML Configuration                                   *
 *        Un-comment the below code when authentication mode is saml.          *
 *                                                                             *
 *******************************************************************************/

String keystore_pass = new File("/saml/keystore/password").text.trim()
banner.sso.authentication.saml.localLogout='true'
grails {
	plugin {
		springsecurity {
			failureHandler {
				defaultFailureUrl = '/login/error'
			}
			auth {
				loginFormUrl = '/saml/login'
			}
			saml {
				active = true
				afterLogoutUrl = '/logout/customLogout'
				maxAuthenticationAge = 43200
				
				keyManager {
					storeFile = 'file:/saml/keystore/keystore.jks'
					storePass = keystore_pass
					passwords = [ 'zdevl-generalss-sp': keystore_pass ]
					defaultKey = 'zdevl-generalss-sp'
				}
				metadata {
					providers = [adfs: '/saml/metadata/idp.xml']
					defaultIdp = 'https://sts.windows.net/ac352f9b-eb63-4ca2-9cf9-f4c40047ceff/'

					sp {
						file = '/saml/metadata/sp.xml'
						defaults = [
							local: true,
							alias: 'zdevl-generalss-sp',
							securityProfile: 'metaiop',
							signingKey: 'zdevl-generalss-sp',
							encryptionKey: 'zdevl-generalss-sp',
							tlsKey: 'zdevl-generalss-sp',
							requireArtifactResolveSigned: false,
							requireLogoutRequestSigned: false,
							requireLogoutResponseSigned: false
						]
					}
				}
			}
		}
	}
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
 *                                                                              *
 *                 Text Manager Configuration                                   *
 *                                                                              *
 ***************************************************************************** **/
/*
Below configurations are required for an application in order to enable Text Manager Translations

    *  enableTextManagerTranslations
        To Enable Text Manager translations, set to false if its not required for an application.
        setting it to false completely disables the translations from Text Manager in both MEP and Non-MEP environment

    *  enableTextManagerTranslationsInMEP
        To Enable Text Manager translations in MEP environment for an application.
        set to true if the TextManager tables are MEPed and Translations are required as per institution.
*/

enableTextManagerTranslations = true
enableTextManagerTranslationsInMEP = false

/***************************************************************************************

 REDIS TENANT-ID CONFIGURATION

 ***************************************************************************************/

//App teams need to specify unique  tenant Id and App Id specific to Self Service App
//tenantId = <<TENANT_ID>>
//spring.session.redis.namespace='spring:session:'+tenantId+':<<APP ID>>'

/*********************************************************************************
 *     X-Frame-Options header config for Grails 7                               *
 /********************************************************************************/

 xframeOptionsProtectedPaths = ['/login/auth']
 def enableXFrameOptions = true // or false
 grails.plugin.springsecurity.headers = [
 xframeOptions: enableXFrameOptions ? 'DENY' : null
 ]

