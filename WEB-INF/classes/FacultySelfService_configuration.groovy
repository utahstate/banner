/**************************************************************************************
    Copyright 2018-2019 Ellucian Company L.P. and its affiliates.
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

    * Self Service Support
    * CAS SSO Configuration (supporting administrative and self service users)    
    * Authentication Provider Configuration
    * Cas Configuration   
    * Extensibility Extensions & I18n File Location
    * Saml Configuration
    * Home Page Link
    * Configjob

    NOTE: DataSource and JNDI configuration resides in the cross-module
    'banner_configuration.groovy' file.

*********************************************************************************** **/

/** ***********************************************************************************
 *                                                                                    *
 *                              Self Service Support                                  *
 *                                                                                    *
 *********************************************************************************** **/
ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)



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
    }
}

if(banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' )
{
    grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
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
                  serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/FacultySelfService/login/cas'
                  serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') 
                  proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + '/FacultySelfService/secure/receptor'
                  loginUri         = '/login'
                  sendRenew        = false
                  proxyReceptorUrl = '/secure/receptor'
                  useSingleSignout = true
                  key = 'grails-spring-security-cas'
                  artifactParameter = 'SAMLart'
                  serviceParameter = 'TARGET'
                  serverUrlEncoding = 'UTF-8'
                  filterProcessesUrl = '/login/cas'
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
       extensions = "/home/oracle/config_extn/ssb/extensions"
       resources  = "/home/oracle/config_extn/ssb/i18n"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
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
 *                      ConfigJob (Platform 9.23)                               *
 *     Support for configurations to reside in the database.                    *
 *                                                                              *
 ***************************************************************************** **/
configJob.delay = 60000
configJob.interval = 120000
configJob.actualCount = -1


/********************************************************************************
*                                                                               *
*                           Target Server                                       *
********************************************************************************/
/** *****************************************************************************
 *                                                                              *
 *                Application Server Configuration                              *
 * When deployed to Tomcat, targetServer="tomcat"                               *
 * When deployed to WebLogic, targetServer="weblogic"                           *
 *                                                                              *
 ***************************************************************************** **/
targetServer="tomcat"

/********************************************************************************
*                                                                               *
*                      ConfigJob (Platform 9.29)                                *
* Used in BannerDS to wrap dbase calls in locale ( or not )                     *
* Performance implications.  SS applications should set to true.                *
* If loaded to GUROCFG - requires restart.
*                                                                               *
******************************************************************************* **/
enableNLS=true