/**************************************************************************************
    Copyright 2018 Ellucian Company L.P. and its affiliates.
 **************************************************************************************/

/** ***********************************************************************************
 *                                                                                    *
 *            Self-Service Banner 9 Faculty Self-Service Configuration                *
 *                                                                                    *
 **************************************************************************************/

/** ***********************************************************************************

This file contains configuration needed by the Self-Service Banner 9 Faculty Grade Entry
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * JMX Bean Names
    * Self Service Support
    * CAS SSO Configuration (supporting administrative and self service users)
    * Logging Configuration
    * Authentication Provider Configuration
    * Cas Configuration
    * Logging Configuration
    * Extensibility Extensions & I18n File Location
    * Saml Configuration
    * Home Page Link
    * Theme Server Support
    * Google Analytics
    * Configjob
    * Seeddata Keys Configuration

    NOTE: DataSource and JNDI configuration resides in the cross-module
    'banner_configuration.groovy' file.

*********************************************************************************** **/

/** ***********************************************************************************
 *                                                                                    *
 *                              JMX Bean Names                                        *
 *                                                                                    *
 *********************************************************************************** **/

/*
    The names used to register Mbeans must be unique for all applications deployed
    into the JVM.  This configuration should be updated for each instance of each
    application to ensure uniqueness.
*/
jmx {
    exported {
        log4j = "BannerFacultySelfServiceApp-log4j"
    }
}


/** ***********************************************************************************
 *                                                                                    *
 *                              Self Service Support                                  *
 *                                                                                    *
 *********************************************************************************** **/
 ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
 ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)
ssbPassword.reset.enabled = true //true  - allow Pidm users to reset their password.
                                 //false - throws functionality disabled error message


/** ***********************************************************************************
 *                                                                                    *
 *    Tab to preselect when loading the page                                          *
 *                                                                                    *
 *    midterm - Midterm Grades                                                        *
 *    final - Final Grades                                                            *
 *    gradebook - Gradebook                                                           *
 *                                                                                    *
 *********************************************************************************** **/
facultyGradeEntry.preSelectedTabId = (System.getenv('FACULTYGRADEENTRY_PRESELECTEDTABID') ?: 'final')


/** ***********************************************************************************
 *    To hide/display Midterm columns on composite grade roster                       *
 *    false - To hide Midterm columns.                                                *
 *    true - To display Midterm columns.                                              *
 ************************************************************************************ **/
facultyGradeEntry.displayMidterm = (System.getenv('FACULTYGRADEENTRY_DISPLAYMIDTERM') ?Boolean.parseBoolean(System.getenv('FACULTYGRADEENTRY_DISPLAYMIDTERM')) : false)


/** *****************************************************************************************************
 *    To hide/display Student Academic Review page.                                                     *
 *    false - No user will be able to access the Student Academic Review page.                          *
 *    true -  All users can access the page. If this is not configured then true is the default value.  *
 ********************************************************************************************************/
showStudentAcademicReview = t(System.getenv('SHOWSTUDENTACADEMICREVIEW') ?Boolean.parseBoolean(System.getenv('SHOWSTUDENTACADEMICREVIEW')) : true)


/** ***********************************************************************************
 *                                                                                    *
 *                AUTHENTICATION PROVIDER CONFIGURATION                               *
 *                                                                                    *
 *********************************************************************************** **/
//
// Set authenticationProvider to either default, cas or saml.
// If using cas or saml, Either the CAS CONFIGURATION or the SAML CONFIGURATION
// will also need configured/uncommented as well as set to active.
//
banner {
    sso {
        authenticationProvider = 'cas' //  Valid values are: 'default', 'cas', 'saml'
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        if(authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
        }
    }
}

/** ***********************************************************************************
 *                                                                                    *
 *                             CAS CONFIGURATION                                      *
 *                                                                                    *
 *********************************************************************************** **/
grails {
    plugin {
        springsecurity {
            cas {
                  active = true
                  serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                  serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/FacultySelfService/j_spring_cas_security_check"
                  serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                  proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/FacultySelfService/secure/receptor"
                  loginUri         = '/login'
                  sendRenew        = false
                  proxyReceptorUrl = '/secure/receptor'
                  useSingleSignout = true
                  key = 'grails-spring-security-cas'
                  artifactParameter = 'SAMLart'
                  serviceParameter = 'TARGET'
                  serverUrlEncoding = 'UTF-8'
                  filterProcessesUrl = '/j_spring_cas_security_check'
                  if ( active && useSingleSignout){
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


/** ***********************************************************************************
 *                                                                                    *
 *                          LOGGING CONFIGURATION                                     *
 *                                                                                    *
 *********************************************************************************** **/
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

String loggingFileDir =  "/usr/log/tomcat"
String logAppName = "FacultySelfService"
String loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()

log4j = {
    appenders {
        rollingFile name:'appLog', file:loggingFileName, maxFileSize:"${10*1024*1024}", maxBackupIndex:10, layout:pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch( grails.util.Environment.current ) {
        case grails.util.Environment.DEVELOPMENT:
            root {
                off 'stdout','appLog'
                additivity = true
            }
            info  'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            break
        case grails.util.Environment.TEST:
            root {
                error 'stdout','appLog'
                additivity = true
            }
            break
        case grails.util.Environment.PRODUCTION:
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

//Access the student profile page through clicking on student name hyperlink or contact card view profile button click.
//To ensure that the Banner Student Faculty Grade Entry module can communicate with the Banner Student Student Profile module,
// modify the bannerXE.url.mapper.studentProfile parameter to point to the full path to the Banner Student Student Profile module
//bannerXE.url.mapper.studentProfile = 'http://<server>:<port>/<StudentSelfService_root>/ssb/studentProfile'

all.studentcard.cardEnabled.view=['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.cardEnabled.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.name.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.name.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.photo.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.photo.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.major.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.major.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.program.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.program.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.address.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.address.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.telephone.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.telephone.roles= ['faculty','facultyoverride','facultyadvisor']
all.studentcard.email.view= ['gradeEntry','studentAcademicReview','facultyAttendanceRoster']
all.studentcard.email.roles= ['faculty','facultyoverride','facultyadvisor']

/** ***********************************************************************************
 *                                                                                    *
 *                      Extensibility extensions & i18n file location                 *
 *                                                                                    *
 *********************************************************************************** **/
webAppExtensibility {
    locations {
                extensions = "path to the directory location where extensions JSON files will be written to and read from"
                resources = "path to the directory location where i18n files will be written to and read from"
    }
}

/** ***********************************************************************************
 *                                                                                    *
 *                        SAML CONFIGURATION                                          *
 *        Un-comment the below code when authentication mode is saml.                 *
 *                                                                                    *
 *********************************************************************************** **/
 // set active = true when authentication provider section configured for saml
/*grails.plugin.springsecurity.saml.active = false
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


/** ***********************************************************************************
 *                                                                                    *
 *           Home Page link when error happens during authentication.                 *
 *                                                                                    *
 *********************************************************************************** **/
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: '<HOME_URL>' )


/** ***********************************************************************************
 *                                                                                    *
 *   This setting is needed if the application needs to work inside                   *
 *   Application Navigator and the secured application pages will be accessible       *
 *   as part of the single-sign on solution.                                          *
 *                                                                                    *
 *********************************************************************************** **/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


/** *****************************************************************************
 *                                                                              *
 *           Theme server support ( Platform 9.19, 9.20.2, 9.28.5)                             *
 *                                                                              *
 ***************************************************************************** **/
banner.theme.url="http://<hostname>:<port>/<application_name>/ssb/theme"
  // Required only if theme server is remote. If empty the url defaults to current application.
banner.theme.name = "<UPDATE_ME>"
  // This is the desired theme name to use. In a MEP environment, the application uses the MEP code in addition to the theme name.
  // Themes by MEP codes must be created in the Theme Editor on the server specified by banner.theme.url
  // Eg: For SOUTH MEP code with banner.theme.name="default" we need to create a theme in theme editor with name "defaultSOUTH"
banner.theme.template = "FacultySelfService-<update_app_version>" // Eg: FacultySelfService-9_8
  // This is the name of the SCSS (template) file in war file.
banner.theme.cacheTimeOut = 120 // Number in seconds. Eg: 120
  // Required only if the app is the theme server.
  // The value indicates how long the CSS file, that was generated using the template and the theme, is cached.

/** *****************************************************************************
 *                                                                              *
 *               Google Analytics (Platform 9.20)                               *
 *                                                                              *
 ***************************************************************************** **/
banner.analytics.trackerId=''
banner.analytics.allowEllucianTracker=true

/** *****************************************************************************
 *                                                                              *
 *                      ConfigJob (Platform 9.23)                               *
 *     Support for configurations to reside in the database.                    *
 *                                                                              *
 ***************************************************************************** **/
configJob.delay = 60000
configJob.interval = 120000
configJob.actualCount = -1

/** *****************************************************************************
 *                                                                              *
 *                    SeedData Keys Configuration                           *
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
    ['ssbPassword.reset.enabled'],
    ['defaultWebSessionTimeout'],
    ['banner.picturesPath'],
    ['grails.plugin.springsecurity.interceptUrlMap'],
    ['banner.theme.url'],
    ['banner.theme.name'],
    ['banner.theme.template'],
    ['banner.theme.cacheTimeOut'],
    ['banner.analytics.trackerId'],
    ['banner.analytics.allowEllucianTracker']
]


/*********************************************************************************
You must define all global configurations as part of the
ssconfig.global.seeddata.keys property using the same patterns

*********************************************************************************/
ssconfig.global.seeddata.keys =[['footerFadeAwayTime']]
