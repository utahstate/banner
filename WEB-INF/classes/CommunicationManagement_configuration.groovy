/*********************************************************************************
 Copyright 2015-2017 Ellucian Company L.P. and its affiliates.
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
// into the JVM.  This configuration should be updated for each instance of each
// application to ensure uniqueness.
jmx {
    exported {
        log4j = "communication-management-directory-log4j"
    }
}

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************


ssbEnabled = (System.getenv("SSBENABLED") ?: true )
ssbOracleUsersProxied = (System.getenv("SSBORACLEUSERSPROXIED") ?: true )
ssbPassword.reset.enabled = (System.getenv("SSBPASSWORD_RESET_ENABLED") ?: true ) //true  - allow Pidm users to reset their password.
                                 //false - throws functionality disabled error message



/** *****************************************************************************
 *                                                                              *
 *                AUTHENTICATION PROVIDER CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default or cas
banner {
    sso {
		authenticationProvider = (System.getenv("BANNER_SSO_AUTHENTICATIONPROVIDER") ?: 'default' )
        authenticationAssertionAttribute = ( System.getenv("BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE") ?: 'UDC_IDENTIFIER' )
        if(authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
        }
	}
}


// ******************************************************************************
//
//                       +++ CAS CONFIGURATION +++
//
// ******************************************************************************

grails.plugin.springsecurity.saml.active = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_SAML_ACTIVE') ?: false )
grails {
    plugin {
        springsecurity {
            cas {
              active = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE') ?: false )
              serverUrlPrefix  = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX') ?: 'http://CAS_HOST:PORT/cas')
              serviceUrl       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check' )
              serverName       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME') ?: 'http://BANNER9_HOST:PORT' )
              proxyCallbackUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/secure/receptor')
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/j_spring_cas_security_check'
                serverUrlEncoding = 'UTF-8'
                if (active && useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
		    logout {
                afterLogoutUrl = (System.getenv('GRAILS_PLUGIN_LOGOUT_AFTERLOGOUTURL') ?: 'https://cas-server/logout?url=http://myportal/main_page.html' )
                mepErrorLogoutUrl = 'https://URL:PORT/'
            }
        }
    }
}

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
    }
}

// ******************************************************************************
//
//                       +++ LOGGER CONFIGURATION +++
//
// ******************************************************************************
String loggingFileDir =  "target"

String loggingFileName = "${loggingFileDir}/logs/${logAppName}.log".toString()


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
        rollingFile name:'appLog', file:loggingFileName, maxFileSize:"${10*1024*1024}", maxBackupIndex:10, layout:pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch( grails.util.Environment.current.name.toString() ) {
        case 'development':
            root {
                off 'stdout','appLog'
                additivity = true
            }
            info  'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            break
        case 'test':
            root {
                error 'stdout','appLog'
                additivity = true
            }
            break
        case 'production':
            root {
                error 'appLog'
                additivity = true
            }
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
    error 'net'
    // off 'net.hedtech.banner.general.communication'
    // off 'net.hedtech.banner.general.asynchronous'
    // off 'net.hedtech.banner.general.communication.groupsend'
    // off 'net.hedtech.banner.general.communication.field'
    // off 'net.hedtech.banner.general.communication.job'
    // off 'net.hedtech.banner.communication.CommunicationControllerUtility'
    off  'BannerUiSsGrailsPlugin'

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
defaultWebSessionTimeout = (System.getenv('DEFAULTWEBSESSIONTIMEOUT') ?: 1500)


/** *****************************************************************************
 *                                                                              *
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://URL:PORT/')

/**********************************************************************************
***    Google Analytics                                                            *
***********************************************************************************/
banner.analytics.trackerId=(System.getenv('BANNER_ANALYTICS_TRACKERID') ?: "")
banner.analytics.allowEllucianTracker=(System.getenv('BANNER_ANALYTICS_ALLOWELLUCIANTRACKER') ?: true)
/*
banner.analytics.trackerId="<institution's google analytics tracker ID - default blank>"
banner.analytics.allowEllucianTracker=<true|false - default true>
*/
/**********************************************************************************
***    Theming                                                          *
***********************************************************************************/
banner.theme.url=(System.getenv('BANNER_THEME_URL') ?: "http://ThemeServer:8080/BannerExtensibility/theme" )
banner.theme.name=(System.getenv('BANNER_THEME_NAME') ?: "default" )
banner.theme.template=(System.getenv('BANNER_THEME_TEMPLATE') ?: "all" )
/*

banner.theme.url="http://<hostname>:<port>/<application_name>/theme"
banner.theme.name="<theme_name>" (e.g. ellucian)
banner.theme.template="<theme_template_name>"
banner.theme.cacheTimeOut=<time_interval_in_seconds"> (e.g. 120)
*/
