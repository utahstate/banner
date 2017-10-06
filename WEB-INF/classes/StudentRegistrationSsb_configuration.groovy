/** *****************************************************************************
 Copyright 2011-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

 /** ****************************************************************************
 *                                                                              *
 *          Self-Service Banner 9 Registration Configuration                    *
 *                                                                              *
 ***************************************************************************** **/

productName='Student'
banner.applicationName='StudentRegistrationSsb'

/** ****************************************************************************

This file contains configuration needed by the Self-Service Banner 9 Registration
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * Self Service Support

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

    * CAS SSO Configuration (supporting administrative and self service users)

    * Bookstore Link configuration for Higher Education Opportunity Act (HEOA)

    * Update Student Term Data

     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.

***************************************************************************** **/

/** ****************************************************************************
 *                                                                             *
 *                              JMX Bean Names                                 *
 *                                                                             *
 **************************************************************************** **/
//
// The names used to register Mbeans must be unique for all applications deployed
// into the JVM.  This configuration should be updated for each instance of each
// application to ensure uniqueness.
//
jmx {
    exported {
        log4j = "StudentRegistrationSsb-log4j"
    }
}


/** ****************************************************************************
 *                                                                             *
 *                          Logging CONFIGURATION                              *
 *                                                                             *
 **************************************************************************** **/
//
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
    def String loggingFileDir  =  (System.getenv('CATALINA_HOME') ?: "target")
    def String logAppName      = "StudentRegistrationSsb"
    def String loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()
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
    off 'net.hedtech.banner.student.common'
    off 'net.hedtech.banner.student.registration'
    off 'net.hedtech.banner.student.schedule'
    off 'net.hedtech.banner.student.faculty'
    off 'net.hedtech.banner.student.generalstudent'
    off 'net.hedtech.banner.student.system'
    off 'net.hedtech.banner.representations'
    off 'BannerUiSsGrailsPlugin'

    // ******** Connection details to Degreeworks API for Registration Planning *********
    off 'net.hedtech.banner.student.registration.RegistrationRestfulClientCommonUtilityService'

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


/** *****************************************************************************
 *                                                                              *
 *               Supplemental Data Support Enablement                           *
 *                                                                              *
 ***************************************************************************** **/
// Default is false for ssbapplications.
sdeEnabled=(System.getenv('SDEENABLED') ? Boolean.parseBoolean(System.getenv('SDEENABLED')): false )


/** *****************************************************************************
 *                                                                              *
 *    Banner 8 SS Student Account link                                          *
 *                                                                              *
 ***************************************************************************** **/
banner8.SS.studentAccountUrl = (System.getenv('BANNER8_SS_STUDENTACCOUNTURL') ?: "http://<host_name>:<port_number>/<banner8ssb>/twbkwbis.P_GenMenu?name=bmenu.P_ARMnu")


/** *****************************************************************************
 *                                                                              *
 *                AUTHENTICATION PROVIDER CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default, cas or saml.
// If using cas or saml, Either the CAS CONFIGURATION or the SAML CONFIGURATION
// will also need configured/uncommented as well as set to active.
//
banner {
    sso {
		authenticationProvider = (System.getenv('BANNER_SSO_AUTHENTICATIONPROVIDER') ?: 'default')
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
                if (useSingleSignout){
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


/** *****************************************************************************
 *                                                                              *
 *                        BOOKSTORE CONFIGURATION                               *
 *                                                                              *
 ***************************************************************************** **/
//
// The Higher Education Opportunity Act (HEOA) requires that at least one bookstore link be configured.
// This section must be configured to display a hyperlink to an online book site for reviewing required course materials.
//  url: [REQUIRED] enter a valid URL to the bookstore
//  label: [REQUIRED] message.properties code for displaying as hyperlink label
//  campus: [OPTIONAL] provide specific links based on the campus code
//  page: [REQUIRED if params used] Overall Page and Field Configuration Page number if the URL is specific to search results ("30" - Section)
//  params: [OPTIONAL] There are three options for identifying parameters to be substituted in the URL.
//       1.  Overall Page and Field Configuration Field (SOAWSCR) value for parameter substitution or one of the following options.
//           (INSTRUCTOR and MEETINGTIME are not valid options)
//       2.  Student name may be passed to the bookstore link by supplying "NAME" in the parameter list.  The name will be formatted
//           as identified in the General Person plugin messages.properties for default.name.format which is used throughout
//           Banner XE administrative and SSB apps.
//       3. If the element you need is not in SOAWSCR, you may identify it directly from the section object in the code.
//           Examples:
//              ".termDesc" - section.termDesc (201410)
//              ".partOfTerm" - section.partOfTerm (1)
//              ".subject" - section.subject (ART)
//              ".campus" - section.campus (M)
//              ".campusDescription" - section.campusDescription (Main Campus)
//              ".college" - section.college (BU)
//
// If using variables, use "{0}", "{1}", and so on, as place holders for the parameters appearing in the same order in the params array.
//
// NOTE:  These are examples of possible link configurations.
// 1. Borders will only be displayed if the section is associated with campus code = 1.
// 2. Barnes and Noble will be displayed with all sections.
// 3. Follett book site will require substitution of "<<Campus Store ID>>" with the actual campus ID, as well as parameters which will be substituted at run time.
//
bookstore = [
[
  url: "http://usu.verbacompare.com/comparison?trm={0}&catids={1}",
  label: "bookstore.links.usu.comparison",
  params: [".termDesc", "COURSEREFERENCENUMBER"],
  page: "30",
]
]

/** *****************************************************************************
 *                                                                              *
 *                        Email and Print CONFIGURATION                         *
 *                                                                              *
 ***************************************************************************** **/
//
//Allow printing and email on My Schedule and Options - and on Lookup a Schedule page.
//These sections must be configured to allow for either, or both features to be enabled.
//Mail host must be your SMTP server - "your.smtp.address" would be your universities email address, like mail.school.edu, or a valid IP address to the mail server
//Default from will be the email address, that when sent to students, will display in the "FROM" field of the email.
//This email address must also be a valid email address within your DNS/email server, preferably an email account that is not actively monitored.
//
grails {
   mail {
     host = (System.getenv('GRAILS_MAIL_HOST') ?: 'mailhost.sct.com')
   }
}
grails.mail.default.from= (System.getenv('GRAILS_MAIL_DEFAULT_FROM') ?: 'firstname.lastname@ellucian.com')
allowPrint = (System.getenv('ALLOWPRINT') ? Boolean.parseBoolean(System.getenv('ALLOWPRINT')): true )
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED') ? Boolean.parseBoolean(System.getenv('SSBPASSWORD_RESET_ENABLED')): true )       //true - allow Pidm users to reset their password.      false - throws functionality disabled error message

/** *****************************************************************************
 *                                                                              *
 *                    UPDATE STUDENT TERM DATA CONFIGURATION                    *
 *                                                                              *
 ***************************************************************************** **/
//
//  Banner XE Registration SSB will provide clients the option of offering students
//  access to a BXE page for updating their student term data.
//  To enable this link from the XE Registration Prepare for Registration page,
//  set to 'Y'.  If the Update Term Data should not be accessible from XE, set to 'N'.
//
updateStudentTermData = (System.getenv('UPDATESTUDENTTERMDATA') ?: 'N')


/** *****************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

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
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://BANNER9_HOME:PORT/StudentRegistrationSsb' )

/** ****************************************************************************
 *                                                                              *
 *              Transaction timeout Configuration (in seconds)                  *
 *                                                                              *
 ***************************************************************************** **/
defaultWebSessionTimeout = (System.getenv('DEFAULTWEBSESSIONTIMEOUT') ?Integer.parseInt(System.getenv('DEFAULTWEBSESSIONTIMEOUT')): 15000 )

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
 *           Theme server support ( Platform 9.19, 9.20.2)                             *
 *                                                                              *
 ***************************************************************************** **/
 banner.theme.url=(System.getenv('BANNER_THEME_URL') ?: "http://ThemeServer:8080/pathTo/ssb/theme" )
 banner.theme.name=(System.getenv('BANNER_THEME_NAME') ?: "default" )
 banner.theme.template=(System.getenv('BANNER_THEME_TEMPLATE') ?: "all" )
 banner.theme.cacheTimeOut=120 //Replace time_interval_in_seconds with a number like 120

/** *****************************************************************************
 *                                                                              *
 *               Google Analytics (Platform 9.20)                               *
 *                                                                              *
 ***************************************************************************** **/
 banner.analytics.trackerId=(System.getenv('BANNER_ANALYSTICS_TRACKERID') ?: '')
 banner.analytics.allowEllucianTracker=(System.getenv('BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER') ? Boolean.parseBoolean(System.getenv('BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER')): true)

/** *****************************************************************************
 *                                                                              *
 *                      ConfigJob (Platform 9.23)                               *
 *                                                                              *
 ***************************************************************************** **/
 configJob.delay = 60000
 configJob.interval = 120000
 configJob.actualCount = -1
