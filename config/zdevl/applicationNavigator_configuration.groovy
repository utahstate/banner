/*********************************************************************************
Copyright 2014-2024 Ellucian Company L.P. and its affiliates.
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
ssbEnabled = true
myLinksEnabled = true

// This setting is set to false for Application Navigator deployment by default.
// Only set 'ssbOracleUsersProxied = true' to ensure that database connections
// are proxied when the SS user has an oracle account and there is a strong need
// to set FGAC for Application Navigator menus.
//
// This setting in Application Navigator has no impact on integrated Banner 9 Self-
// Service applications. The integrated Banner 9 Self-Service applications can be
// configured separately to allow FGAC on the application specific SSB pages.
ssbOracleUsersProxied = false

/** *********************************************************************************
  Set 'isExperienceIntegrated' to true for accessing the SSB application only in
  Experience. Set to false to access the SSB application in standalone mode.
  Default value is 'false'
************************************************************************************ */
isExperienceIntegrated = false

/** *****************************************************************************
 *                                                                              *
 *                        OAuth2 configuration                               *
 *                                                                              *
 ***************************************************************************** **/
banner.oauth2.issuerJwksURi= "https://oauth.prod.10005.elluciancloud.com/jwks"
banner.oauth2.issuer = "https://oauth.prod.10005.elluciancloud.com"
banner.oauth2.audiance="https://elluciancloud.com"

/********************************************************************************
 *                                                                              *
 *                Banner Authentication Provider Configuration                  *
 *                                                                              *
 ********************************************************************************/
//
boolean ssoEnabled = false

if(System.getenv('AUTH_METHOD') == 'saml')
{
    banner {
        sso {
            authenticationProvider           = 'saml' //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
            authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        }
    }
}
if(System.getenv('AUTH_METHOD') == 'cas')
{
    banner {
        sso {
            authenticationProvider           = 'cas' //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
            authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        }
    }
}
if(banner.sso.authenticationProvider == 'cas' || banner.sso.authenticationProvider == 'saml' ){
    ssoEnabled = true
    }
 if(ssoEnabled)
    {
    grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
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
grails.plugin.springsecurity.logout.afterLogoutUrl = '/logout/customLogout'



/********************************************************************************
 *                                                                              *
 *           Home Page URL configuration for CAS / SAML Single-Sign On          *
 *                                                                              *
 ********************************************************************************/
// Can be institutional home page ex: http://myportal/main_page.html
grails.plugin.springsecurity.homePageUrl=(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL') ?: 'http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/' )


/********************************************************************************
 *                                                                              *
 *                         SAML2 SSO Configuration                              *
 *                                                                              *
 ********************************************************************************/
//Set active = true when Application Navigator is configured for SAML2 SSO
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
					passwords = [ 'zdevl-appnav-sp': keystore_pass ]
					defaultKey = 'zdevl-appnav-sp'
				}
				metadata {
					providers = [adfs: '/saml/metadata/idp.xml']
					defaultIdp = 'https://sts.windows.net/ac352f9b-eb63-4ca2-9cf9-f4c40047ceff/'

					sp {
						file = '/saml/metadata/sp.xml'
						defaults = [
							local: true,
							alias: 'zdevl-appnav-sp',
							securityProfile: 'metaiop',
							signingKey: 'zdevl-appnav-sp',
							encryptionKey: 'zdevl-appnav-sp',
							tlsKey: 'zdevl-appnav-sp',
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
seamless.footerFadeAwayTime = 0  // Footer fade away time in milliseconds. A value of less than or equal to 0 indicates that the footer will not fade away.
seamless.defaultInfoTextLocale = "en_US" //This is an example for configuring default locale for custom text on the landing page.

// Configure the brand title with a default institution value or based on the MEP institution code configured in the Banner database
// To add values by MEP code, append the name:value pair to the existing seamless.brandTitle property. Example
// Example: seamless.brandTitle=["Default": "Ellucian University","<MEP_CODE>":"<MEP_BRAND_TITLE>", "<MEP_CODE>":"<MEP_BRAND_TITLE>"]
seamless.brandTitle=["Default": "Ellucian University"]

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
//grails.plugin.xframeoptions.deny = true
//grails.plugin.xframeoptions.urlPattern = '/login/auth'

/*********************************************************************************
 *     X-Frame-Options header config for Grails 7                               *
 /********************************************************************************/

 xframeOptionsProtectedPaths = ['/login/auth']
 def enableXFrameOptions = true // or false
 grails.plugin.springsecurity.headers = [
 xframeOptions: enableXFrameOptions ? 'DENY' : null
 ]
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



/***Use this seeddata section to move required configurations to Database. **/
ssconfig.app.seeddata.keys = [['seamless.sessionTimeout': 30],['seamless.sessionTimeoutNotification': 5],['seamless.messageTimer': 60],
                                ['banner.theme.url'],
                                ['banner.theme.name': 'mytheme'],
                                ['banner.theme.template': 'applicationname'],
                                ['seamless.dbInstanceName': "Ellucian DataBase"],
                                ['productName': 'Banner General'],
                                ['banner.applicationName': 'Application Navigator']]

/********************************************************************************
*              Theming Configuration                                            *
********************************************************************************/
banner.theme.url="https://theme.elluciancloud.com/<AccountApiID>/theme"


/********************************************************************************
*                     NLS Configuration                                         *
********************************************************************************/
enableNLS = false


/*********************************************************************************
*                     Application Server Configuration                    *
* When deployed on Tomcat this configuration should be targetServer="tomcat"     *
**********************************************************************************/
targetServer="tomcat"

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
defaultWebSessionTimeout = 15000

/** *************************************************************************************
 *                                                                                      *
 *                        REDIS TENANT-ID CONFIGURATION                                           *
 ************************************************************************************* **/
//App teams need to pass the tenantId and AppId specific to them
tenantId = '<<client_tenant>>'
spring.session.redis.namespace='spring:session:'+tenantId+':AppNav'
