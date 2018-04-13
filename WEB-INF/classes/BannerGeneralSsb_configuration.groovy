/*********************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/

/** ****************************************************************************
 *                                                                              *
 *          Self-Service Banner 9 General Self-Service Configuration            *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

 This file contains configuration needed by the Self-Service Banner 9 General
 web application. Please refer to the administration guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:


 * JMX Bean Names
 * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)
 * Self Service Support
 * Supplemental Data Support Enablement
 * CAS SSO Configuration (supporting administrative and self service users)
 * SAML SSO Configuration (supporting administrative and self service users)
 * Web Application Extensibility
 * Home page link when error happens during authentication
 * Configuration to use themes served by the Theme Server
 * Google Analytics
 * Eliminate access to the WEB-INF folder
 * Banner 8 SS URLs
 * MEP Setup
 * Location of users' profile picture image files
 * Config Job configuration



 NOTE: DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.

 * Self Service Support

 * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

 * CAS SSO Configuration (supporting administrative and self service users)

 ***************************************************************************** **/

/** ****************************************************************************
 *                                                                             *
 *                              JMX Bean Names                                 *
 *                                                                             *
 *******************************************************************************/

// The names used to register Mbeans must be unique for all applications deployed
// into the JVM.  This configuration should be updated for each instance of each
// application to ensure uniqueness.
jmx {
    exported {
        log4j = "BannerGeneralSsb-log4j"
    }
}

/** ****************************************************************************
 *                                                                             *
 *                          Logging Configuration                              *
 *                                                                             *
 **************************************************************************** **/
logAppName = "BannerGeneralSsb"
String loggingFileDir = "/usr/local/tomcat/logs"
String loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()

// Note that logging is configured separately for each environment ('development', 'test', and 'production').
// By default, all 'root' logging is 'off'.  Logging levels for root, or specific packages/artifacts, should be configured via JMX.
// Note that you may enable logging here, but it:
//   1) requires a restart, and
//   2) will report an error indicating 'Cannot add new method [getLog]'. (although the logging will in fact work)
//
// JMX should be used to modify logging levels (and enable logging for specific packages). Any JMX client, such as JConsole, may be used.
//
// The logging levels that may be configured are, in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
//
log4j = {
    appenders {
        rollingFile name: 'appLog', file: loggingFileName, maxFileSize: "${10 * 1024 * 1024}", maxBackupIndex: 10, layout: pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch (grails.util.Environment.current.name.toString()) {
        case 'development':
            root {
                off 'stdout', 'appLog'
                additivity = true
            }
            info 'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            break
        case 'test':
            root {
                error 'stdout', 'appLog'
                additivity = true
            }
            break
        case 'production':
            root {
                error 'appLog'
                additivity = true
            }
            debug 'net.hedtech.banner.aip.filter.GateKeepingFilters'
            error 'grails.app.service'
            error 'grails.app.controller'
            info 'net.hedtech.banner.representations'
            info 'net.hedtech.banner.supplemental.SupplementalDataService'
            break
    }

    // Log4j configuration notes:
    // The following are some common packages that you may want to enable for logging in the section above.
    // You may enable any of these within this file (which will require a restart),
    // or you may add these to a running instance via JMX.
    //
    // Note that settings for specific packages/artifacts will override those for the root logger.
    // Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.

    // ******** non-Grails classes (e.g., in src/ or grails-app/utils/) *********
    off 'net.hedtech.banner.service'
    off 'net.hedtech.banner.student'
    off 'net.hedtech.banner.student.catalog'
    off 'net.hedtech.banner.student.faculty'
    off 'net.hedtech.banner.student.generalstudent'
    off 'net.hedtech.banner.student.system'
    off 'net.hedtech.banner.representations'
    off 'BannerUiSsGrailsPlugin'

    // ******** Grails framework classes *********
    off 'org.codehaus.groovy.grails.web.servlet'        // controllers
    off 'org.codehaus.groovy.grails.web.pages'          // GSP
    off 'org.codehaus.groovy.grails.web.sitemesh'       // layouts
    off 'org.codehaus.groovy.grails.web.mapping.filter' // URL mapping
    off 'org.codehaus.groovy.grails.web.mapping'        // URL mapping
    off 'org.codehaus.groovy.grails.commons'            // core / classloading
    off 'org.codehaus.groovy.grails.plugins'            // plugins
    off 'org.codehaus.groovy.grails.orm.hibernate'      // hibernate integration
    off 'org.springframework'                           // Spring IoC
    off 'org.hibernate'                                 // hibernate ORM
    off 'grails.converters'                             // JSON and XML marshalling/parsing
    off 'grails.app.service.org.grails.plugin.resource' // Resource Plugin
    off 'org.grails.plugin.resource'                    // Resource Plugin

    // ******* Security framework classes **********
    off 'net.hedtech.banner.security'
    off 'net.hedtech.banner.db'
    off 'net.hedtech.banner.security.BannerAccessDecisionVoter'
    off 'net.hedtech.banner.security.BannerAuthenticationProvider'
    off 'net.hedtech.banner.security.CasAuthenticationProvider'
    off 'net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider'
    off 'grails.plugin.springsecurity'
    off 'org.springframework.security'
    off 'org.apache.http.headers'
    off 'org.apache.http.wire'

    // Grails provides a convenience for enabling logging within artefacts, using 'grails.app.XXX'.
    // Unfortunately, this configuration is not effective when 'mixing in' methods that perform logging.
    // Therefore, for controllers and services it is recommended that you enable logging using the controller
    // or service class name (see above 'class name' based configurations).  For example:
    //     all  'net.hedtech.banner.testing.FooController' // turns on all logging for the FooController
    //
    // debug 'grails.app' // apply to all artefacts
    // debug 'grails.app.<artefactType>.ClassName // where artefactType is in:
    //                   bootstrap  - For bootstrap classes
    //                   dataSource - For data sources
    //                   tagLib     - For tag libraries
    //                   service    // Not effective with mixins -- see comment above
    //                   controller // Not effective with mixins -- see comment above
    //                   domain     - For domain entities
}

/** *****************************************************************************
 *                                                                              *
 *                         Self Service Support                                 *
 *                                                                              *
 ***************************************************************************** **/


ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED') ? Boolean.parseBoolean(System.getenv('SSBPASSWORD_RESET_ENABLED')): true ) //true  - allow Pidm users to reset their password.
//false - throws functionality disabled error message

// In CAS and SAML modes, set to true to allow API calls to bypass single sign-on authentication
guestAuthenticationEnabled = (System.getenv('GUESTAUTHENTICATIONENABLED') ?Boolean.parseBoolean(System.getenv('GUESTAUTHENTICATIONENABLED')) : false)

// Product and application name to refer to this app for name display rules
productName = "General"
banner.applicationName = "GeneralSsb"
footerFadeAwayTime = 30000

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
 *               Supplemental Data Support Enablement                           *
 *                                                                              *
 ***************************************************************************** **/
// Default is false for ssbapplications.
sdeEnabled=(System.getenv('SDEENABLED') ? Boolean.parseBoolean(System.getenv('SDEENABLED')): false )

/** *****************************************************************************
 *                                                                              *
 *                AUTHENTICATION PROVIDER CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default or cas.
// If using cas, the CAS CONFIGURATION
// will also need configured/uncommented as well as set to active.
//
banner {
    sso {
        authenticationProvider = 'cas' //  Valid values are: 'default', 'cas', or 'saml'
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        if (authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
        }
        if(authenticationProvider == 'saml') {
            grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
        }
    }
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
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/StudentRegistrationSsb/j_spring_cas_security_check"
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/StudentRegistrationSsb/secure/receptor"
                loginUri = '/login'
                sendRenew = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                serverUrlEncoding = 'UTF-8'
                filterProcessesUrl = '/j_spring_cas_security_check'
                if (active && useSingleSignout) {
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                afterLogoutUrl = (System.getenv('BANNER9_AFTERLOGOUTURL') ?:  'https://cas-server/logout?url=http://myportal/main_page.html' )
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
/*
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
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

/** *****************************************************************************
 *                                                                              *
 *   Add admin roles as required by web-app-extensibility SecurityService in    *
 *   non-production environment.                                                *
 *   See:  http://confluence.ellucian.com/display/banner/                       *
 *         Steps+to+include+the+web-app-extensibility+plugin+to+an+application  *
 *                                                                              *
 ***************************************************************************** **/
webAppExtensibility {
    locations {
        extensions = "path to the directory location where extensions JSON files will be written to and read from"
        resources = "path to the directory location where i18n files will be written to and read from"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}

// ******************************************************************************
//                                                                              *
//                       +++ HOME PAGE URL CONFIGURATION +++                    *
//             Home page link when error happens during authentication.         *
//                                                                              *
// ******************************************************************************
grails.plugin.springsecurity.homePageUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://BANNER9_HOME:PORT/StudentRegistrationSsb' )

/** *****************************************************************************
 *                                                                              *
 *                   Theme Server Support                                       *
 *                                                                              *
 ***************************************************************************** **/
banner.theme.url=(System.getenv('BANNER_THEME_URL') ?: "http://ThemeServer:8080/pathTo/ssb/theme" ) //Required only if theme server is remote.
//References the URL to the application hosting the Theme Server Example : http://hostname:port/BannerGeneralSsb/ssb/theme
banner.theme.name=(System.getenv('BANNER_THEME_NAME') ?: "default" ) // This is the desired theme name to use. In a MEP environment, the application uses the MEP code as the theme name instead of the banner.theme.name . A theme by this name must be created in the Theme Editor on the server specified by banner.theme.url
banner.theme.template=(System.getenv('BANNER_THEME_TEMPLATE') ?: "BannerGeneralSsb" ) // This is the name of the scss file containing the theme settings in war file.
banner.theme.cacheTimeOut = 120 // seconds, required only if the app is theme server. The value indicates how long the CSS file that was generated using the template and the theme is cached.

/******************************************************************************
 *                               Google Analytics                              *
 *******************************************************************************/
banner.analytics.trackerId=(System.getenv('BANNER_ANALYSTICS_TRACKERID') ?: '')
banner.analytics.allowEllucianTracker=(System.getenv('BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER') ? Boolean.parseBoolean(System.getenv('BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER')): true)

/*******************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

/*******************************************************************************
 *                                                                              *
 *              Page Builder Artifact File Location Configuration               *
 *                                                                              *
 *******************************************************************************/
pageBuilder.enabled = (System.getenv('PAGEBUILDER_ENABLED') ? Boolean.parseBoolean(System.getenv('PAGEBUILDER_ENABLED')): true)
if (!pageBuilder.enabled) {
  grails.plugin.springsecurity.securityConfigType = grails.plugin.springsecurity.SecurityConfigType.InterceptUrlMap
}
// initial load location on file system. Files located in "pb" directory. pb directory located at root of app as sibling to the config files
pbRoot = (System.getenv('PBROOT') ?: '/opt/banner/pb') // Example /temp/pb
pageBuilder {
    locations {
      bundle        = "${pbRoot}/i18n"
      page          = "${pbRoot}/page"
      css           = "${pbRoot}/css"
      virtualDomain = "${pbRoot}/virtdom"
    }
    // Uncomment debugRoles to reveal detailed SQL error messages for
    // Virtual domains to users with any of the comma separated roles
    // debugRoles = "ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M"
}


/** *****************************************************************************
 *                                                                              *
 *                 Configuration for AIP application                     *
 *                                                                              *
 ***************************************************************************** **/
BCMLOCATION=(System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/CommunicationManagement' // The URL of BCM application. Please note, Banner Communication Manangement (BCM) is dependent application for General SS 9.2.
                                                         //Example localhost:8080/CommunicationManagement
BANNER_AIP_BLOCK_PROCESS_PERSONA= ['EVERYONE', 'STUDENT', 'REGISTRAR', 'FACULTYINSTRUCTOR', 'FACULTYADVISOR', 'FACULTYBOTH'] // Add/update if any change in persona
BANNER_AIP_EXCLUDE_LIST='aipActionItemPosting|aipAdmin|aip|aipPageBuilder|BCM|about|cssManager|cssRender|error|excelExportBase|dateConverter|keepAlive|login|logout|resetPassword|securityQa|selfServiceMenu|survey|test|theme|themeEditor|userAgreement|userPreference'// No change in this.
// in case of new controller which needs to be ignored, can be added here.
GENERALLOCATION=(System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') '+/BannerGeneralSsb' // Example localhost:8080/BannerGeneralSsb
ssconfig.app.seeddata.keys = [['BCMLOCATION'], ['BANNER_AIP_BLOCK_PROCESS_PERSONA']]

/*******************************************************************************
 *                                                                             *
 *        Config Job  Configurations                                            *
 *                                                                             *
 *******************************************************************************/
configJob.delay = 60000
configJob.interval = 120000
configJob.actualCount = -1

aip {
    weblogicDeployment = false

    actionItemPostMonitor {
        enabled = (System.getenv('AIP_ACTIONITEMPOSTMONITOR_ENABLED') ? Boolean.parseBoolean(System.getenv('AIP_ACTIONITEMPOSTMONITOR_ENABLED')) : false )
        monitorIntervalInSeconds = 10
    }

    actionItemPostWorkProcessingEngine {
        enabled = (System.getenv('AIP_ACTIONITEMPOSTWORKPROCESSINGENGINE_ENABLED') ? Boolean.parseBoolean(System.getenv('AIP_ACTIONITEMPOSTWORKPROCESSINGENGINE_ENABLED')) : false )
        maxThreads = 1
        maxQueueSize = 5000
        continuousPolling = true
        pollingInterval = 2000
        deleteSuccessfullyCompleted = false
    }

    actionItemJobProcessingEngine {
        enabled = (System.getenv('AIP_ACTIONITEMJOBPROCESSINGENGINE_ENABLED') ? Boolean.parseBoolean(System.getenv('AIP_ACTIONITEMJOBPROCESSINGENGINE_ENABLED')) : false )
        maxThreads = 2
        maxQueueSize = 5000
        continuousPolling = true
        pollingInterval = 2000
        deleteSuccessfullyCompleted = false
    }

    scheduler {
        enabled = (System.getenv('AIP_SCHEDULER_ENABLED') ? Boolean.parseBoolean(System.getenv('AIP_SCHEDULER_ENABLED')) : false )
        idleWaitTime = 30000
    }
}
