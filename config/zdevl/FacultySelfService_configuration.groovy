/**************************************************************************************
    Copyright 2018-2022 Ellucian Company L.P. and its affiliates.
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
banner {
    sso {
        authenticationProvider           = 'saml' //  Valid values are: 'saml' and 'cas' for SSO to work. 'default' to be used only for zip file creation.
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
    }
}

/** ***********************************************************************************
 *                                                                                    *
 *                      Extensibility extensions & i18n file location                 *
 *                                                                                    *
 *********************************************************************************** **/
 webAppExtensibility {
    locations {
       //extensions = "/home/oracle/config_extn/ssb/extensions"
       extensions = "/usr/local/tomcat/webapps/"
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
					passwords = [ 'zdevl-facultyss-sp': keystore_pass ]
					defaultKey = 'zdevl-facultyss-sp'
				}
				metadata {
					providers = [adfs: '/saml/metadata/idp.xml']
					defaultIdp = 'https://sts.windows.net/ac352f9b-eb63-4ca2-9cf9-f4c40047ceff/'

					sp {
						file = '/saml/metadata/sp.xml'
						defaults = [
							local: true,
							alias: 'zdevl-facultyss-sp',
							securityProfile: 'metaiop',
							signingKey: 'zdevl-facultyss-sp',
							encryptionKey: 'zdevl-facultyss-sp',
							tlsKey: 'zdevl-facultyss-sp',
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

/********************************************************************************
*                                                                               *
*                           Target Server                                       *
********************************************************************************/
/** *****************************************************************************
 *                                                                              *
 *                Application Server Configuration                              *
 * When deployed to Tomcat, targetServer="tomcat"                               *
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
banner.applicationName="Faculty Self Service"
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

/** *************************************************************************************
 *                                                                                      *
 *                        Text Manager Translations MEP CONFIGURATION                   *
 ************************************************************************************* **/

//enableTextManagerTranslations will be set to true by default. Setting to false completely disables the translations from Text Manager in both MEP and Non-MEP environments.
//enableTextManagerTranslationsInMEP will be set to false by default. Only customers, who opt to MEP the underlying text manager-specific tables, will need to turn to flag to true and verify translations are institution-specific.

enableTextManagerTranslations = true
enableTextManagerTranslationsInMEP = false


/***************************************************************************************

 REDIS TENANT-ID CONFIGURATION

 ***************************************************************************************/
// App teams need to specify unique  tenant Id and App Id specific to Self Service App
//tenantId = <<TENANT_ID>>
//spring.session.redis.namespace='spring:session:'+tenantId+':FACSS'
