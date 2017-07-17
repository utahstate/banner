/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/******************************************************************************
 This file contains configuration needed by the Banner XE Page Builder
 web application. Please refer to the administration guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:
 * PageBuilder
     *   Enable/Disable switch and DataSource connection details for Page Builder artifacts
     *   Miscellaneous configuration

 * Theme
     *   url:  theme server url, if this is not specified then it points to same app
     *   theme: used by client application, mep code is used as theme name by default
     *   template: used by client application
     *   cacheTimeOut: themes would be cached for specified duration (in seconds) in theme server
                       (This is not required if the app points to remote theme server)

 * Self Service Support
 * CAS SSO Configuration (supporting self service users)
 * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

 NOTE: Banner DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.

 *******************************************************************************/

pageBuilder.enabled = (System.getenv('PAGEBUILDER_ENABLED').asBoolean() ?: true)

if (!pageBuilder.enabled) {
  grails.plugin.springsecurity.securityConfigType = grails.plugin.springsecurity.SecurityConfigType.InterceptUrlMap
}

/*******************************************************************************
 *                                                                              *
 *              Page Builder Artifact File Location Configuration               *
 *                                                                              *
 *******************************************************************************/
pbRoot = (System.getenv('PBROOT') ?: '/temp/pb')
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

/*******************************************************************************
 *                                                                              *
 *              Theme Configuration                                             *
 *                                                                              *
 *******************************************************************************/
 banner.theme.url=(System.getenv('BANNER_THEME_URL') ?: 'http://BANNER9_HOST:PORT/BannerExtensibility/theme')
 banner.theme.name=(System.getenv('BANNER_THEME_NAME') ?: 'ellucian')
 banner.theme.template=(System.getenv('BANNER_THEME_TEMPLATE') ?: 'BannerExtensibility')
 banner.theme.cacheTimeOut = (System.getenv('BANNER_THEME_CACHETIMEOUT') ?: 900)

/*environments {
     production {
         banner.theme.url="http://BANNER9_HOST:PORT/BannerExtensibility/theme"   // required only if theme server is remote
         banner.theme.name="ellucian"                                       // Not required for MEP
         banner.theme.template="BannerExtensibility"
         banner.theme.cacheTimeOut = 900                                    // seconds, required only if the app is theme server
     }
     development {
         banner.theme.url="http://BANNER9_HOST:PORT/BannerExtensibility/theme"  // required only if theme server is remote
         banner.theme.name="ellucian"                                      // Not required for MEP
         banner.theme.template="BannerExtensibility"
         banner.theme.cacheTimeOut = 120                                   // seconds, required only if the app is theme server
     }
}*/




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
        log4j = "BannerExtensibility-log4j"
    }
}

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************


ssbEnabled = (System.getenv('SSBENABLED').asBoolean() ?: true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED').asBoolean() ?: true)
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED').asBoolean() ?: true) //true  - allow Pidm users to reset their password.
                                 //false - throws functionality disabled error message


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
        authenticationProvider           = (System.getenv('BANNER_SSO_AUTHENTICATIONPROVIDER') ?: 'default')  //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
        authenticationAssertionAttribute = (System.getenv('BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE') ?: 'UDC_IDENTIFIER')
        if(authenticationProvider != 'default') {
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
                active = (Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE') ?: false ))
                serverUrlPrefix  = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX') ?: 'http://CAS_HOST:PORT/cas' )
                serviceUrl       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check')
                serverName       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME') ?: 'http://BANNER9_HOST:PORT' )
                proxyCallbackUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/secure/receptor' )
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT').asBoolean() ?:true)
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
                afterLogoutUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL') ?: 'http://CAS_HOST:PORT/cas/logout?url=http://BANNER9_HOST:PORT/APP_NAME/')
                // afterLogoutUrl = '/' // This can be used to navigate to the landing page when not using CAS
            }
        }
    }
}

grails.plugin.springsecurity.homePageUrl= (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/')

//This setting contains the institution-specific redirect URL for MEP if Return Home is clicked.
grails.plugin.springsecurity.logout.mepErrorLogoutUrl = '/logout/customLogout'

//guestAuthenticationEnabled = true

// This entry is required to ensure that 'Sign In' link takes you to corresponding login i.e. for CAS,
// any other authentication system or Default (Banner).
// Example navigates to home page after sign in
loginEndpoint=(System.getenv('LOGINENDPOINT') ?: 'http://BANNER9_HOST:PORT/APP_NAME/customPage/page/pbadm.ssoauth?url=/' )


/*******************************************************************************
 *                                                                              *
 *              SAML CONFIGURATION                                                    *
 *                                                                              *
 *******************************************************************************/
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'
banner.sso.authentication.saml.localLogout='false'
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'extensibility'
grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/bekeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'password'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'extensibility': 'password' ]
grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-BannerExtensibility-sp.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security/banner-BannerExtensibility-idp.xml']
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
    local: true,
    alias: 'extensibility',
    securityProfile: 'metaiop',
    signingKey: 'extensibility',
    encryptionKey: 'extensibility',
    tlsKey: 'extensibility',
    requireArtifactResolveSigned: false,
    requireLogoutRequestSigned: false,
    requireLogoutResponseSigned: false
]

/*******************************************************************************
 *                                                                              *
 *              X-Frame-Options                                                 *
 *                                                                              *
 *******************************************************************************/
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


// ******************************************************************************
//
//                       +++ LOGGER CONFIGURATION +++
//
// ******************************************************************************
String loggingFileDir =  (System.getenv('CATALAINA_HOME') ?: '/target')
String logAppName = "BannerExtensibility"
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
        rollingFile name:'appLog',
        file:loggingFileName,
        maxFileSize:"${10*1024*1024}",
        maxBackupIndex:10,
        layout:pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    root {
        error 'stdout','appLog'
        additivity = true
    }



    // Log4j configuration notes:
    // The following are some common packages that you may want to enable for logging in the section above.
    // You may enable any of these within this file (which will require a restart),
    // or you may add these to a running instance via JMX.
    //
    // Note that settings for specific packages/artifacts will override those for the root logger.
    // Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.

    // ******** non-Grails classes (e.g., in src/ or grails-app/utils/) *********
    warn 'net.hedtech.banner.service'
    warn 'net.hedtech.banner.student'
    warn 'net.hedtech.banner.student.catalog'
    warn 'net.hedtech.banner.student.common'
    warn 'net.hedtech.banner.student.registration'
    warn 'net.hedtech.banner.student.schedule'
    warn 'net.hedtech.banner.student.faculty'
    warn 'net.hedtech.banner.student.generalstudent'
    warn 'net.hedtech.banner.student.system'
    warn 'net.hedtech.banner.representations'
    warn 'BannerUiSsGrailsPlugin'

    // ******** Grails framework classes *********
    error 'org.codehaus.groovy.grails.web.servlet'        // controllers
    error 'org.codehaus.groovy.grails.web.pages'          // GSP
    error 'org.codehaus.groovy.grails.web.sitemesh'       // layouts
    error 'org.codehaus.groovy.grails.web.mapping.filter' // URL mapping
    error 'org.codehaus.groovy.grails.web.mapping'        // URL mapping
    error 'org.codehaus.groovy.grails.commons'            // core / classloading
    error 'org.codehaus.groovy.grails.plugin'            // plugins
    error 'org.codehaus.groovy.grails.orm.hibernate'      // hibernate integration
    error 'org.springframework'                           // Spring IoC
    error 'org.hibernate'                                 // hibernate ORM
    error 'grails.converters'                             // JSON and XML marshalling/parsing
    off   'grails.app.service.org.grails.plugin.resource' // Resource Plugin
    off   'org.grails.plugin.resource'                    // Resource Plugin
    error 'org.hibernate.type'
    error 'org.hibernate.SQL'

    // ******* Security framework classes **********
    error 'net.hedtech.banner.security'
    error  'net.hedtech.banner.db'
    error 'net.hedtech.banner.security.BannerAccessDecisionVoter'
    error 'net.hedtech.banner.security.BannerAuthenticationProvider'
    error 'net.hedtech.banner.security.CasAuthenticationProvider'
    error 'net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider'
    error 'grails.plugin.springsecurity'
    error 'org.springframework.security'
    error 'org.apache.http.headers'
    error 'org.apache.http.wire'

    // **** Banner and PageBuilder classes ****
    error 'net.hedtech.banner.menu'
    error 'net.hedtech.banner.tools'
    error 'net.hedtech.banner.sspb'
    error 'net.hedtech.banner.virtualDomain'
    error 'net.hedtech.tools'

    // Grails provides a convenience for enabling logging within artefacts, using 'grails.app.XXX'.
    // Unfortunately, this configuration is not effective when 'mixing in' methods that perform logging.
    // Therefore, for controllers and services it is recommended that you enable logging using the controller
    // or service class name (see above 'class name' based configurations).  For example:
    //     all  'net.hedtech.banner.testing.FooController' // turns on all logging for the FooController
    //
    off 'grails.app' // apply to all artefacts
    // debug 'grails.app.<artefactType>.ClassName // where artefactType is in:
    //                   bootstrap  - For bootstrap classes
    //                   dataSource - For data sources
    //                   tagLib     - For tag libraries
    //                   service    // Not effective with mixins -- see comment above
    //                   controller // Not effective with mixins -- see comment above
    //                   domain     - For domain entities
}

/*******************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 *******************************************************************************/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']


/************************************************************
             Web Application Extensibility
************************************************************/
webAppExtensibility {
    // Comma separated list of roles
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}
