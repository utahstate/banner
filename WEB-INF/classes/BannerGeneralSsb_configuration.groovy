/*********************************************************************************
 Copyright 2015-2017 Ellucian Company L.P. and its affiliates.
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

    * Self Service Support

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

    * CAS SSO Configuration (supporting administrative and self service users)

     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.

***************************************************************************** **/

logAppName = "BannerGeneralSsb"

// Product and application name to refer to this app for name display rules
productName="General"
banner.applicationName="GeneralSsb"

footerFadeAwayTime=30000

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
        log4j = "BannerGeneralSsb-log4j"
    }
}

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************


ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED') ? Boolean.valueOf(System.getenv('SSBPASSWORD_RESET_ENABLED')): true) //true  - allow Pidm users to reset their password.
                                 //false - throws functionality disabled error message

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
        authenticationProvider           = (System.getenv('BANNER_SSO_AUTHENTICATIONPROVIDER') ?: 'default') //  Valid values are: 'default', 'cas', or 'saml'
        authenticationAssertionAttribute = (System.getenv('BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE')?:'UDC_IDENTIFIER')
        if(authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
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
                active = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE') ? Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE')) : false )
                serverUrlPrefix  = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check')
                serverName       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/secure/receptor')
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT') ? Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT')) :true)
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                serverUrlEncoding = 'UTF-8'
                filterProcessesUrl = '/j_spring_cas_security_check'
                if (active && useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                afterLogoutUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL') ?: 'https://cas-server/logout?url=http://myportal/main_page.html')
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

// ******************************************************************************
//                                                                              *
//                       +++ HOME PAGE URL CONFIGURATION +++                    *
//             Home page link when error happens during authentication.         *
//                                                                              *
// ******************************************************************************
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://HOST:PORT/' )

// ******************************************************************************
//
//                       +++ LOGGER CONFIGURATION +++
//
// ******************************************************************************
String loggingFileDir =  (System.getenv('CATALINA_HOME') ?: "target")
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
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

banner8.SS.url = (System.getenv('BANNER8_SS_URL') ?: 'http://<host_name>:<port_number>/<banner8>/')

// ********************************************************************************
//    MEP Setup
// ********************************************************************************
//To use MEP context sensitive 8.x SSB URLs in menus uncomment the mep.banner8.SS.url lines
//  For example, if we have Banner 9 General Self Service App deployed with two
//  MEP institutions (GVU and BANNER) having the following URL syntax.
//  http://example.ellucian.com:8105/BannerGeneralSsb?mepCode=BANNER
//  http://example.ellucian.com:8105/BannerGeneralSsb?mepCode=GVU

/***********************************************************************
mep.banner8.SS.url = [
    GVU : 'http://example.ellucian.com:8888/ssomanager/c/SSB?pkg=http://example.ellucian.com:9020/SMPL_GVU/',
    BANNER : 'http://example.ellucian.com:8888/ssomanager/c/SSB?pkg=http://example.ellucian.com:9020/SMPL_BANNER/'
]
************************************************************************/

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

/** *****************************************************************************
 *                                                                              *
 *                   Theme Server Support                                       *
 *                                                                              *
 ***************************************************************************** **/
banner.theme.url=(System.getenv('BANNER_THEME_URL') ?: "http://BANNER9_HOST:PORT/ExampleApp/theme")
banner.theme.name= (System.getenv('BANNER_THEME_NAME') ?: "default")
banner.theme.template=(System.getenv('BANNER_THEME_TEMPLATE') ?: "BannerGeneralSsb" )

// ********************************************************************************
// Cross-frame scripting vulnerability when integrating with Application Navigator.
// ********************************************************************************
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true

// ********************************************************************************
// Location of users' profile picture image files
// ********************************************************************************
banner {
  picturesPath=(System.getenv('BANNER_PICTUREPATH') ?: '/test/images')
  // defaultPhoto = <Fully qualified path to the photo to use if no user photo is available.>
}

/*******************************************************************************
 *                                                                             *
 *                          Google Analytics                                   *
 *                                                                             *
 *******************************************************************************/
banner.analytics.trackerId=(System.getenv('BANNER_ANALYTICS_TRACKERID') ?: '')
banner.analytics.allowEllucianTracker=(System.getenv('BANNER_ANALYTICS_ALLOWELLUCIANTRACKER') ?Boolean.parseBoolean(System.getenv('BANNER_ANALYTICS_ALLOWELLUCIANTRACKER')): true )
