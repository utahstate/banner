/** *****************************************************************************
 Copyright 2014-2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

/** ****************************************************************************
 *                                                                              *
 *          Banner 9 Finance Self-Service Configuration                         *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

 This file contains configuration needed by the Self-Service Banner 9 Registration
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
 * Configuration to use themes served by the Theme Server
 * Google Analytics
 * Hibernate Secondary Level Caching
 * BDM Configurations
 * My Finance Common configuration
 * My Requisition configuration
 * My Finance Query configuration

 NOTE: DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.
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
        log4j = "BannerFinanceSSB-log4j"
    }
}

/** ****************************************************************************
 *                                                                             *
 *                          Logging Configuration                              *
 *                                                                             *
 **************************************************************************** **/
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
    def String loggingFileDir = (System.getenv('CATALINA_HOME') ?: "target")
    def String logAppName = "BannerFinanceSSB"
    def String loggingFileName = "${loggingFileDir}/logs/${logAppName}.log".toString()
    appenders {
        rollingFile name: 'appLog', file: loggingFileName, maxFileSize: "${10 * 1024 * 1024}", maxBackupIndex: 10, layout: pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch (grails.util.Environment.current.name.toString()) {
        case 'development':
            root {
                error 'stdout', 'appLog'
                additivity = true
            }
            info 'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            trace 'net.hedtech.banner.overall'
            debug 'grails.app.service'
            debug 'grails.app.controller'

            break
        case 'test':
            root {
                error 'stdout', 'appLog'
                additivity = true
            }
            break
        case 'production':
            root {
                error 'stdout', 'appLog'
                additivity = true
            }
            trace 'net.hedtech.banner.overall'
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

    trace 'net.hedtech.banner.overall'
    // ******** non-Grails classes (e.g., in src/ or grails-app/utils/) *********
    off 'net.hedtech.banner.service'
    off 'net.hedtech.banner.representations'
    off 'BannerUiSsGrailsPlugin'

    // ******** Grails framework classes *********
    off 'org.codehaus.groovy.grails.web.servlet'        // controllers
    off 'org.codehaus.groovy.grails.web.pages'          // GSP
    off 'org.codehaus.groovy.grails.web.sitemesh'       // layouts
    off 'org.codehaus.groovy.grails.web.mapping.filter' // URL mapping
    off 'org.codehaus.groovy.grails.web.mapping'        // URL mapping
    off 'org.codehaus.groovy.grails.commons'            // core / classloading
    off 'org.codehaus.groovy.grails.plugin'            // plugin
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
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED') ? Boolean.valueOf(System.getenv('SSBPASSWORD_RESET_ENABLED')): true)        //true - allow Pidm users to reset their password. false - throws functionality disabled error message

/** *****************************************************************************
 *                                                                              *
 *   This setting is needed if the application needs to work inside             *
 *   Application Navigator and the secured application pages will be accessible *
 *   as part of the single-sign on solution.                                    *
 *                                                                              *
 ***************************************************************************** **/
banner.applicationName = "Banner Finance"
productName = "Finance"
defaultWebSessionTimeout = 1400
footerFadeAwayTime = 4000
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


/** *****************************************************************************
 *                                                                              *
 *               Supplemental Data Support Enablement                           *
 *                                                                              *
 ***************************************************************************** **/
// Default is false for ssbapplications.
sdeEnabled = (System.getenv('SDEENABLED') ? Boolean.valueOf(System.getenv('SDEENABLED')): false)

/** *****************************************************************************
 *                                                                              *
 *                BANNER AUTHENTICATION PROVIDER CONFIGURATION                	*
 *                                                                              *
 ***************************************************************************** **/
//
// Set authenticationProvider to either default, cas or saml
// If you are using cas then the CAS CONFIGURATION also need to be configured/uncommented as well as set to active.
// If you are using saml then the SAML CONFIGURATION also need to be configured/uncommented as well as set to active.
//
banner {
    sso {
        authenticationProvider = (System.getenv('BANNER_SSO_AUTHENTICATIONPROVIDER') ?: 'default') //  Valid values are: 'default', 'cas' and 'saml'
        authenticationAssertionAttribute = (System.getenv('BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE')?:'UDC_IDENTIFIER')
        if (authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
        }
        if (authenticationProvider == 'saml') {
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
                active = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE') ? Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE')) : false )
                serverUrlPrefix = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check')
                serverName = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/secure/receptor')
                loginUri = '/login'
                sendRenew = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT') ? Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT')) :true)
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                serverUrlEncoding = 'UTF-8'
                filterProcessesUrl = '/j_spring_cas_security_check'
                if (useSingleSignout) {
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                afterLogoutUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL') ?: 'https://cas-server/logout?url=http://myportal/main_page.html')
            }
        }
    }
}

/** *************************************************************************************
 *                                                                                      *
 *                        SAML CONFIGURATION                                            *
 *  Un-comment the below code and set active = true when authentication mode is saml.   *
 *                                                                                      *
 ************************************************************************************* **/
/*
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'

banner.sso.authentication.saml.localLogout='false' // To disable single logout set this to true,default 'false'.

grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/<KEY_NAME>.jks'  // for unix File based Example:- 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-<short-appName>-sp': 'changeit' ]  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-<short-appName>-sp'                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup

grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-<Application_Name>-sp.xml'            // for unix file based Example:-'/home/u02/sp-local.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security/banner-<Application_Name>-idp.xml'] // for unix file based Example: '/home/u02/idp-local.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
        local: true,
        alias: 'banner-<short-appName>-sp',                  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        securityProfile: 'metaiop',
        signingKey: 'banner-<short-appName>-sp',             // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        encryptionKey: 'banner-<short-appName>-sp',          // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        tlsKey: 'banner-<short-appName>-sp',                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        requireArtifactResolveSigned: false,
        requireLogoutRequestSigned: false,
        requireLogoutResponseSigned: false
]*/

/** ***************************************************************************
 *               Web Application Extensibility                                  *
 *******************************************************************************/

webAppExtensibility {
    locations {
        extensions = "C:/BanXE/Extensions/ss_ext/extensions/"
        // for unix based Example:-'/home/oracle/config_extn/ssb/extensions/'
        resources = "C:/BanXE/Extensions/ss_ext/i18n/"
        // for unix based Example:-'/home/oracle/config_extn/ssb/i18n/'
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}

// URL of application home page Example: http://localhost:port/BannerFinanceSSB
grails.plugin.springsecurity.homePageUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEULR') ?: 'http://localhost:port/BannerFinanceSSB')

/******************************************************************************
 *   Configuration to use themes served by the Theme Server                   *
 ***************************************************************************** **/
banner.theme.url = (System.getenv('BANNER_THEME_URL') ?: "http://BANNER9_HOST:PORT/ExampleApp/theme") //Required only if theme server is remote.
//References the URL to the application hosting the Theme Server Example : http://hostname:port/BannerFinanceSSB/ssb/theme
banner.theme.name = (System.getenv('BANNER_THEME_NAME') ?: "default") // This is the desired theme name to use. In a MEP environment, the application uses the MEP code as the theme name instead of
// the banner.theme.name . A theme by this name must be created in the Theme Editor on the server specified by banner.theme.url
banner.theme.template = (System.getenv('BANNER_THEME_TEMPLATE') ?: 'BannerFinanceSSB' ) // This is the name of the scss file containing the theme settings in war file.
banner.theme.cacheTimeOut = 120 // seconds, required only if the app is theme server. The value indicates how long the CSS file that was generated using the template and the theme is cached.

/******************************************************************************
 *                               Google Analytics                              *
 *******************************************************************************/
banner.analytics.trackerId = (System.getenv('BANNER_ANALYTICS_TRACKERID') ?: '')            //institution's google analytics tracker ID - default blank
banner.analytics.allowEllucianTracker = (System.getenv('BANNER_ANALYTICS_ALLOWELLUCIANTRACKER') ? Boolean.parseBoolean(System.getenv('BANNER_ANALYTICS_ALLOWELLUCIANTRACKER')): true )    //true|false - default true

/******************************************************************************
 *                   Hibernate Secondary Level Caching                         *
 *******************************************************************************/
hibernate.cache.use_second_level_cache=true  // Default true. Make it false for Institution which is MEP enabled for Finance
hibernate.cache.use_query_cache=true         // Default true. Make it false for Institution which is MEP enabled for Finance

/** *****************************************************************************

 *                  BDM Configurations                                          *

 ***************************************************************************** **/
bdm.enabled = false
bdm.file.location = '<UPDATE ME>' /** Location of BDM Document folder. Example C:/BDM_DOCUMENTS_FOLDER/ on Windows or /u02/Tomcat7/BDM_DOCUMENTS_FOLDER in Linux: */

bdmserver {

    AXWebServicesUrl = 'http://<UPDATE ME>/appxtender/axservicesinterface.asmx'
    /** URL of BDM sever ax service interface */
    AXWebAccessURL = 'http://<UPDATE ME>/appxtender/' /** URL of BDM sever */
    Username = '<UPDATE ME>' /** User Name on BDM sever */
    Password = '<UPDATE ME>' /** User Password on BDM sever */
    BdmDataSource = '<UPDATE ME>' /** Data source */
    KeyPassword = '<UPDATE ME>' /** Key Password */
    AppName = '<UPDATE ME>' /** Application Name */
}

/** *****************************************************************************
 *                                                                              *
 * 			My Finance Common configuration                         *
 *                                                                              *
***************************************************************************** **/

// This is list of supported Finance Applications. [Application_Name_Key: show flag]
// Make show flag as true to list the application in finance common dashboard. false to hide the application from
// finance common dashboard page
banner {
    consolidated {
        applications = [REQUISITION: true, FINANCEQUERY: true] // Applications that have been consolidated
    }
}
/** Name and path of logos for the PDF files. Example D:/PURCHASE_REQUISITION/LOGO/ellucian-logo.png on Windows or /u02/Tomcat7/images/ellucian-logo.png
in Linux: File should have png extension.*/
banner.finance.ssb.logoImageFile = '<UPDATE ME>'

/** *****************************************************************************
 *                                                                              *
 *              My Requisition configuration                                    *
 *                                                                              *
 ***************************************************************************** **/

//Vendor configuration
banner {
    vendor {
        corporateAddressTypes = ['UPDATE_ME']
        // Specify corporate vendor address type codes to filter vendors lookup. For example corporateAddressTypes = ['MA', 'BU']
        personAddressTypes = ['UPDATE_ME']
        // Specify person vendor address type codes to filter vendors lookup. If no person vendor needed personAddressTypes = ['']
        chooseVendorForMeChecked = true
        // Default setting for Choose Vendor For me; true is checked , false is unchecked
    }
    commodity {
        copyItemText = true // copy printable commodity text to public comments, non-printable to private comments
    }
}

//Accounting Level
banner {
    accounting {
        disable = true // Disabling it will not allow user to change the Accounting type : Options true or false
        defaultType = 'document' // Default document type options : document or commodity ( case sensitive)
    }
}

/** *****************************************************************************
*                                                                              *
*                   My Finance Query configuration                             *
*                                                                              *
***************************************************************************** **/
financeQuery {
    barChart {
        includeCommitment = true
    }
    pieChart {
        colorPercentageRange = [Green: 80..1000, Yellow: 60..79, LightningYellow: 40..59, Orange: 20..39, Red: 0..19, DeepRed: -1000..-1]
    }
    queryTypes {
        excludeQueryTypes = [BUDGET_QUERY_ACCOUNT: false, BUDGET_QUERY_ORG: false, PAYROLL_EXPENSE_QUERY: false, ENCUMBRANCE_QUERY: false, BUDGET_MULTI_YEAR_QUERY: false]
    }
    queryList {
        showHealthColumn = true
    }
    excelExport {
        fileType = 'xls' // options are xls or xlsx. If not defined, default is xls
    }
    healthIcon {
        colorPercentageRange = [Green: 61..1000, Yellow: 21..60, Red: -1000..20]
    }
}
