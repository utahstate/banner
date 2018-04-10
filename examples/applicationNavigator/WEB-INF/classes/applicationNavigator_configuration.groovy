/*********************************************************************************
Copyright 2014-2017 Ellucian Company L.P. and its affiliates.
**********************************************************************************/
 
 /*******************************************************************************
 *                                                                              *
 *              Application Navigator Configuration                             *
 *                                                                              *
 ********************************************************************************/

/*
This file contains configuration needed by the Application Navigator
web application. Please refer to the installation guide for additional
information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)
    
    * Administrative and Self Service Endpoint Support Enablement
         
    * CAS / SAML2 SSO Configuration (supporting administrative and self serivce users)

    * Application Navigator seamless plugin configurations

    * Application Navigator Display Name, MEP and X-Frame-Options configurations
    
     NOTE: DataSource and JNDI configuration resides in the cross-module 
           'banner_configuration.groovy' file. 
    
********************************************************************************/


/*******************************************************************************
 *                                                                             *
 *                              JMX Bean Names                                 *
 *                                                                             *
 *******************************************************************************/
// The names used to register Mbeans must be unique for all applications deployed
// into the JVM.  This configuration should be updated for each instance of each
// application to ensure uniqueness.
jmx {
    exported {
        log4j = "applicationNavigator-log4j"
    }
}


/*******************************************************************************
 *                                                                             *                 
 *                          Logging Configuration                              *
 *                                                                             *
 *******************************************************************************/
// Note that logging is configured separately for each environment ('development', 'test', and 'production').
// By default, all 'root' logging is 'error'. level.  
//
// Note that if you change logging configuration directly in this file:
//   1) you will need to restart the application to see the changes, and
//   2) you may see an innocuous error during initialization indicating 'Cannot add new method [getLog]' 
//      (this error does not preclude successful logging and can be safely ignored).
//
// JMX may be used to modify logging levels for specific packages identified below. 
// Any JMX client, such as JConsole, may be used.
//
// The logging levels that may be configured are, in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
//
log4j = {

    def String loggingFileDir  = "/usr/local/tomcat"
    def String logAppName      = "applicationNavigator"
    def String loggingFileName = "${loggingFileDir}/logs/${logAppName}.log".toString()

    appenders {
        rollingFile name:'appLog', file:loggingFileName, maxFileSize:"${10*1024*1024}", maxBackupIndex:10, layout:pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch( grails.util.Environment.current.name.toString() ) {
        case 'development':
            root {
                error 'stdout','appLog'
                additivity = true
            }
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
            break
    }

    // Log4j configuration notes:
    // The following are some common packages that you may want to enable for logging in the section above.
    // You may enable any of these within this file (which will require a restart),
    // or you may add these to a running instance via JMX.
    //
    // Note that settings for specific packages/artifacts will override those for the root logger.
    // Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.

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

    // ******* Security framework classes **********
    off 'net.hedtech.banner.security'
    off 'net.hedtech.banner.db'
    off 'net.hedtech.banner.security.BannerAccessDecisionVoter'
    off 'net.hedtech.banner.security.BannerAuthenticationProvider'
    off 'net.hedtech.banner.security.CasAuthenticationProvider'
    off 'net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider'
    off 'net.hedtech.banner.security.BannerSamlAuthenticationProvider'
    off 'net.hedtech.banner.security.AuthenticationProviderUtility'
    off 'grails.plugin.springsecurity'
    off 'org.springframework.security'
    off 'org.apache.http.headers'
    off 'org.apache.http.wire'
    off 'org.jasig.cas'

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

} // end log4j


/********************************************************************************
 *                                                                              *
 *               Administrative Endpoint Support Enablement                     *
 *                                                                              *
 ********************************************************************************/
// Disabling 'administrativeBannerEnabled' (setting to 'false') will prevent the 
// BannerAuthenticationProvider from attempting to authenticate users. 
//
//administrativeBannerEnabled = false  // default is 'true'


/********************************************************************************
 *                                                                              *
 *                Self Service Endpoint Support Enablement                      *
 *                                                                              *
 ********************************************************************************/
// Set 'ssbEnabled' to true for instances that expose Self Service Banner endpoints. 
// If this is set to false, or if this configuration item is missing, the instance 
// will only support administrative applications and not self service applications
// in the unified menu. 
//
// If this is enabled, Application Navigator will integrate with Banner Self Service
// applications using the SSB datasource. It is important to also ensure the 
// corresponding commonSelfServiceMenu menu endpoint is configured below. 
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : false)

// This setting is set to false for Application Navigator deployment by default.
// Only set 'ssbOracleUsersProxied = true' to ensure that database connections
// are proxied when the SS user has an oracle account and there is a strong need
// to set FGAC for Application Navigator menus.
//
// This setting in Application Navigator has no impact on integrated Banner 9 Self-
// Service applications. The integrated Banner 9 Self-Service applications can be
// configured separately to allow FGAC on the application specific SSB pages.
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : false)


/********************************************************************************
 *                                                                              *
 *                Banner Authentication Provider Configuration                  *
 *                                                                              *
 ********************************************************************************/
//
banner {
    sso {
        authenticationProvider           = 'cas' //  Valid values are: 'saml' and 'cas' for SSO. 'default' value to be used only when creating the release zip file.
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        if(authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
        }
        if(authenticationProvider == 'saml') {
            grails.plugin.springsecurity.auth.loginFormUrl = '/saml/login'
        }
    }
}


/********************************************************************************
 *                                                                              *
 *                Application Navigator Logout URL                              *
 *                                                                              *
 ********************************************************************************/
 // When the banner sso authentication provider is set to "cas", set the value of
 // the afterLogoutUrl property to:
 //  'https://CAS_HOST:PORT/cas/logout?url=http://APPLICATION_NAVIGATOR_HOST:PORT/'
 // For other authentication providers use the setting noted below.
grails.plugin.springsecurity.logout.afterLogoutUrl = (System.getenv('BANNER9_AFTERLOGOUTURL') ?:  'http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/logout/customLogout' )


/********************************************************************************
 *                                                                              *
 *           Home Page URL configuration for CAS / SAML Single-Sign On          *
 *                                                                              *
 ********************************************************************************/
// Can be institutional home page ex: http://myportal/main_page.html
grails.plugin.springsecurity.homePageUrl=(System.getenv('BANNER9_HOMEPAGEURL') ?: 'http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator' )


/********************************************************************************
 *                                                                              *
 *                             CAS SSO Configuration                            *
 *                                                                              *
 ********************************************************************************/
// Set active = true when Application Navigator is configured for CAS SSO  
grails {
    plugin {
        springsecurity {
            cas {
                active           = true
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/applicationNavigator/j_spring_cas_security_check"
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/applicationNavigator/secure/receptor"
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/j_spring_cas_security_check'
                serverUrlEncoding = 'UTF-8'
                if (useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
        }
    }
}

// Cannot be declared bean style because the value is not available for reference in seamless.interceptPattern
grails.plugin.springsecurity.cas.serverUrlPrefix = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')


/********************************************************************************
 *                                                                              *
 *                         SAML2 SSO Configuration                              *
 *                                                                              *
 ********************************************************************************/
// Set active = true when Application Navigator is configured for SAML2 SSO  
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'

banner.sso.authentication.saml.localLogout='false'	 // Setting localLogout to false, allows the application to send a single or global logout request to the Identity Service Provider

grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/samlkeystore.jks'     // for unix based 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-appnav-sp': 'changeit' ]        // banner-appnav-sp is the value set in EIS Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-appnav-sp'                       // banner-appnav-sp is the value set in EIS Service provider setup

grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-appnav-sp.xml'               // for unix based '/home/u02/banner-appnav-sp.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security//banner-appnav-idp.xml']   // for unix based '/home/u02/banner-appnav-idp.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
        local: true,
        alias: 'banner-appnav-sp',                                                                 // banner-appnav-sp is the value set in EIS Service provider setup
        securityProfile: 'metaiop',
        signingKey: 'banner-appnav-sp',                                                            // banner-appnav-sp is the value set in EIS Service provider setup
        encryptionKey: 'banner-appnav-sp',                                                         // banner-appnav-sp is the value set in EIS Service provider setup
        tlsKey: 'banner-appnav-sp',                                                                // banner-appnav-sp is the value set in EIS Service provider setup
        requireArtifactResolveSigned: false,
        requireLogoutRequestSigned: false,
        requireLogoutResponseSigned: false
]


/********************************************************************************
 *                                                                              *
 *              Application Navigator Seamless plugin Configuration             *
 *                                                                              *
 ********************************************************************************/
//
seamless.interceptPattern = "${grails.plugin.springsecurity.cas.serverUrlPrefix}.*"

// When ssbEnabled is set to true, the commonSelfServiceMenu menu endpoint will be 
// invoked along with the administrative commonMenu menu endpoint when loading the 
// menus.
seamless.menuEndpoints = [
        (System.getenv('BANNER9_URL')?: 'http://APPLICATION_NAVIGATOR_HOST:PORT') + "/applicationNavigator/commonMenu",
        (System.getenv('BANNER9_URL')?: 'http://APPLICATION_NAVIGATOR_HOST:PORT') + "/applicationNavigator/commonSelfServiceMenu"
]

// List the URL entries of Banner Self Service Applications integrating with Application
// Navigator.
//
// If Application Navigator is configured with a MEP Database, then the URLs of the
// Self Service Applications listed below must be appended with the MEP code query
// parameter as shown below:
//   "http://SELFSERVICE_APPLICATION_HOST:PORT/<BANNER_SSB_APPLICATION>?mepCode={mepCode}"
//
// The entries added must match those entries listed in the Web Tailor menus without
// which they will not be displayed in the Application Navigator unified menu.
seamless.selfServiceApps = [
     (System.getenv("BANNER9_SS_URL") ?: 'http://APPLICATION_NAVIGATOR_HOST:PORT') + "/BannerExtensibility/"
]

seamless.logLevel="off"
seamless.ajaxTimeout=30000
seamless.messageResponseTimeout=2000
seamless.exposeMenu=true

// Configure the brand title with a default institution value or based on the MEP institution code configured in the Banner database
// To add values by MEP code, append the name:value pair to the existing seamless.brandTitle property. Example
// Example: seamless.brandTitle=["Default": "Ellucian University","<MEP_CODE>":"<MEP_BRAND_TITLE>", "<MEP_CODE>":"<MEP_BRAND_TITLE>"]
seamless.brandTitle=["Default": (System.getenv('SEAMLESS_BRANDTITLE') ?: "Ellucian University" )]

seamless.sessionTimeout = (System.getenv('SEAMLESS_SESSIONTIMEOUT') ? Integer.parseInt(System.getenv('SEAMLESS_SESSIONTIMEOUT')) :30 )           // Session timeout in minutes. A value of -1 indicates session does not timeout
seamless.sessionTimeoutNotification = (System.getenv('SEAMLESS_SESSIONTIMEOUTNOTIFICATION') ? Integer.parseInt(System.getenv('SEAMLESS_SESSIONTIMEOUTNOTIFICATION')) : 5 )  // Notification prompt x minutes before sessionTimeout

// This list includes objects to be excluded from search
seamless.excludeObjectsFromSearch = [
        "GUAGMNU","GUAINIT","GUQSETI","FOQMENU","SOQMENU","TOQMENU","AOQMEMU","GOQMENU","ROQMENU","NOQMENU","POQMENU","FACICON","FAQINVP","FAQMINV","FAQVINV","FGQACTH","FGQAGYH","FGQDOCB","FGQDOCN","FGQDOCP","FGQFNDE","FGQFNDH","FGQLOCH","FGQORGH","FGQPRGH","FOQADDR","FOQDCSR","FOQENCB","FOQFACT","FOQINVA","FOQJVCD","FOQPACT","FOQRACT","FOQSDLF","FOQSDLV","FPCRCVP","FPQBLAP","FPQCHAP","FRCBSEL","FSCISSR","FSCSTKL","FTQATTS","FXQDOCN","FXQDOCP","**SSB_MASKING","TSQCONT","TSQEXPT","TOQCALC","GPBADMN","SFQESTS","SFQPREQ","SFQRQST","SFQRSTS","SFQSECM","SFQSECT","SHQDEGR","SHQQPNM","SHQSECT","SHQSUBJ","SHQTERM","SHQTRAM","SLQBCAT","SLQEVNT","SLQMEET","SLQROOM","SMQSACR","SMQSGCR","SMQSGDF","SMQSPDF","SOQCSCP","SOQCTRM","SOQHOLD","RPQLELG","RPQCOMP","ROQADDR","GMAPRTO"
]


/********************************************************************************
 *                                                                              *
 *               Application Navigator Display Name Configuration               *
 *                                                                              *
 ********************************************************************************/
// Product name and Application Name configured to utilize the display name api
// (for which rule is configured in gurnhir table), which return the username
// in the desired format as per the usage defined in GURNDSP table.
productName='Banner General'                    // Name of the product - The name in the gurnhir table and in this config must match
banner.applicationName='Application Navigator'  // Application Name - The name in the gurnhir table and in this config must match


/********************************************************************************
 *                                                                              *
 *                    Application Navigator MEP Configuration                   *
 *                                                                              *
 ********************************************************************************/
//
mepEnabled = false
grails.plugin.springsecurity.logout.mepErrorLogoutUrl = '/logout/customLogout'


/********************************************************************************
 *                                                                              *
 *              Application Navigator X-Frame-Options Configuration             *
 *                                                                              *
 ********************************************************************************/
// Setting the X-Frame-Options will not expose Application Navigator login page  
// to the clickjacking vulnerability when loaded in an iframe.
grails.plugin.xframeoptions.deny = true
grails.plugin.xframeoptions.urlPattern = '/login/auth'


/********************************************************************************
 *                                                                              *
 *              Spring Security Port Forwarding Configuration                   *
 *                                                                              *
 ********************************************************************************/
// To support port forwarding for the menu service callback URLs,
// uncomment the spring security port mapper configuration shown below
// and specify the port where the HTTP(S) request must be forwarded to.

// grails.plugin.springsecurity.portMapper.httpPort = <PORT_NUMBER>
// grails.plugin.springsecurity.portMapper.httpsPort = <SSL_PORT_NUMBER>


 /********************************************************************************
 *                                                                              *
 *              Google Analytics Configuration                                  *
 *                                                                              *
 ********************************************************************************/
banner.analytics.trackerId=(System.getenv('BANNER_ANALYSTICS_TRACKERID') ?: '')
banner.analytics.allowEllucianTracker=(System.getenv('BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER') ? Boolean.parseBoolean(System.getenv('BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER')): true) // true|false - default true