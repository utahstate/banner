/*********************************************************************************
 Copyright 2013-2017 Ellucian Company L.P. and its affiliates.
 *********************************************************************************/

 /** ****************************************************************************
 *                                                                              *
 *          Self-Service Banner 9 Employee Self-Service Configuration             *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

This file contains configuration needed by the Self-Service Banner 9 Employee
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * Self Service Support

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

    * CAS SSO Configuration (supporting administrative and self service users)

     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.

***************************************************************************** **/

logAppName = "EmployeeSelfService"

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
        log4j = "EmployeeSelfService-log4j"
    }
}

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************


ssbEnabled = (System.getenv('SSBENABLED') ?Boolean.parseBoolean(System.getenv('SSBENABLED')) : true)
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.valueOf(System.getenv('SSBORACLEUSERSPROXIED')) : true)
ssbPassword.reset.enabled = (System.getenv('SSBPASSWORD_RESET_ENABLED') ? Boolean.parseBoolean(System.getenv('SSBPASSWORD_RESET_ENABLED')) : true) //true  - allow Pidm users to reset their password.
                                  //false - throws functionality disabled error message


// *****************************************************************************
//
//                     +++ iframe Denial Setting +++
//
// *****************************************************************************
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


// ******************************************************************************
//
//                       +++ CAS CONFIGURATION +++
//
// ******************************************************************************

banner {
    sso {
		authenticationProvider = (System.getenv('BANNER_SSO_AUTHENTICATIONPROVIDER') ?: 'default')
        authenticationAssertionAttribute = (System.getenv('BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE') ?: 'UDC_IDENTIFIER')
        if(authenticationProvider != 'default') {
            grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/error'
        }
	}
}

grails.plugin.springsecurity.saml.active = false
grails {
    plugin {
        springsecurity {
            cas {
                active = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE') ? Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE')) : false )
                serverUrlPrefix  = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX') ?: 'http://CAS_HOST:PORT/cas' )
                serviceUrl       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check')
                serverName       = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME') ?: 'http://BANNER9_HOST:PORT' )
                proxyCallbackUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL') ?: 'http://BANNER9_HOST:PORT/APP_NAME/secure/receptor' )
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT') ? Boolean.parseBoolean(System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_CAS_USESINGLESIGNOUT')) :true)
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/j_spring_cas_security_check'
                serverUrlEncoding = 'UTF-8'
                if (useSingleSignout){
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                   afterLogoutUrl = (System.getenv('GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL') ?: 'http://CAS_HOST:PORT/cas/logout?url=http://BANNER9_HOST:PORT/APP_NAME/')
                   mepErrorLogoutUrl = '/logout/logoutPage'
            }
        }
    }
}

/** *************************************************************************************
 *                                                                              		*
 *                        SAML CONFIGURATION                                    		*
 *  Un-comment the below code and set active = true when authentication mode is saml.   *
 *                                                                              		*
 ************************************************************************************* **/
/*
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.saml.afterLogoutUrl ='/logout/customLogout'

banner.sso.authentication.saml.localLogout='false'	// To disable single logout set this to true,default 'false'.

grails.plugin.springsecurity.saml.keyManager.storeFile = 'classpath:security/<KEY_NAME>.jks'	// for unix File based Example:- 'file:/home/u02/samlkeystore.jks'
grails.plugin.springsecurity.saml.keyManager.storePass = 'changeit'
grails.plugin.springsecurity.saml.keyManager.passwords = [ 'banner-<short-appName>-sp': 'changeit' ]  // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
grails.plugin.springsecurity.saml.keyManager.defaultKey = 'banner-<short-appName>-sp'                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup

grails.plugin.springsecurity.saml.metadata.sp.file = 'security/banner-<Application_Name>-sp.xml'            // for unix file based Example:-'/home/u02/sp-local.xml'
grails.plugin.springsecurity.saml.metadata.providers = [adfs: 'security/banner-<Application_Name>-idp.xml'] // for unix file based Example: '/home/u02/idp-local.xml'
grails.plugin.springsecurity.saml.metadata.defaultIdp = 'adfs'
grails.plugin.springsecurity.saml.metadata.sp.defaults = [
        local: true,
        alias: 'banner-<short-appName>-sp',     		     // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        securityProfile: 'metaiop',
        signingKey: 'banner-<short-appName>-sp',             // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        encryptionKey: 'banner-<short-appName>-sp',          // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        tlsKey: 'banner-<short-appName>-sp',                 // banner-<short-appName>-sp is the value set in Ellucian Ethos Identity Service provider setup
        requireArtifactResolveSigned: false,
        requireLogoutRequestSigned: false,
        requireLogoutResponseSigned: false
]*/

// ******************************************************************************
//
//                       +++ LOGGER CONFIGURATION +++
//
// ******************************************************************************
String loggingFileDir =  (System.getenv('CATALAINA_HOME') ?: '/target')
String loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()


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
            error 'net.hedtech.banner.pdf'
            error 'org.apache.fop'
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
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            error 'net.hedtech.banner.pdf'
            error 'org.apache.fop'
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



/** *****************************************************************************
 *                                                                              *
 *                 Configurations for Employee Profile                          *
 *                                                                              *
 ***************************************************************************** **/


// Location of employer logos for the Pay Stub PDF.
banner.hr.employerLogoPath = 'http://<host_name>:<port_number>/xxxx'

// Location of Apache FOP base directory for generating PDF documents.
// Leave blank to use the default location.
banner.pdf.fopBaseUrl = ''

banner8.SS.addressUpdateURL = "bwgkogad.P_SelectAtypUpdate"
banner8.SS.addressUpdate = 'N'
banner8.SS.emailUpdateURL = "bwgkogad.P_SelectEmalUpdate"
banner8.SS.emailUpdate = 'N'
banner8.SS.emergencyContactUpdateURL = "bwgkoemr.P_SelectEmrgContacts"
banner8.SS.emergencyContactUpdate = 'N'

//Benefits links HR Dashboard
banner8.SS.benefitsEnrollmentURL = "bwpkdsta.P_ShowEnrollmentMenu"
banner8.SS.benefitsEnrollment8xLinkAvailable = 'Y'
banner8.SS.currentBeneficiariesURL = "bwpkdbcv.P_NamesAndBenefits"
banner8.SS.currentBeneficiaries8xLinkAvailable = 'Y'
banner8.SS.currentSummaryURL = "bwpkebst.P_DispIDSelect"
banner8.SS.currentSummary8xLinkAvailable = 'Y'

//My Activities links HR Dashboard
banner8.SS.enterTimeURL = "bwpktais.P_SelectTimeSheetRoll"
banner8.SS.enterTime8xLinkAvailable = 'Y'
banner8.SS.requestTimeOffURL = "bwpktais.P_SelectLeaveRequestRoll"
banner8.SS.requestTimeOff8xLinkAvailable = 'Y'
banner8.SS.electronicPersonalActionFormsURL = "bwpkepaf.P_DispEpafMenu"
banner8.SS.electronicPersonalActionForms8xLinkAvailable = 'Y'
banner8.SS.facultyLoadCompensationURL = "twbkwbis.P_GenMenu?name=pmenu.P_FacMenu"
banner8.SS.facultyLoadCompensation8xLinkAvailable = 'Y'
banner8.SS.salaryPlannerURL = "twbkwbis.P_GenMenu?name=pmenu.P_SalaMenu"
banner8.SS.salaryPlanner8xLinkAvailable = 'Y'
banner8.SS.effortReportingURL = "bwpkolib.p_launch_flex"
//Effort Reporting 8x Application Link.  NOTE - please disable the Effort Reporting App(9x) link if this one is being enabled.
banner8.SS.effortReporting8xLinkAvailable= 'N'
banner8.SS.laborRedistributionURL = "bwpkolib.p_launch_flex"
//Labor Redistribution 8x Application Link.  NOTE - please disable the labor Redistribution App(9x) link if this one is being enabled.
banner8.SS.laborRedistribution8xLinkAvailable = 'N'
banner8.SS.benefitsAdministratorURL = "bwpkdsta.P_DisplayFilterMain"
banner8.SS.benefitsAdministrator8xLinkAvailable = 'Y'
banner8.SS.morePersonalInfoUpdateURL = "twbkwbis.P_GenMenu?name=bmenu.P_GenMnu"
banner8.SS.morePersonalInfoUpdate = 'Y'
banner8.SS.timeSheetURL = "bwpktais.P_SelectTimeSheetRoll"
banner8.SS.timeSheet8xLinkAvailable = 'Y'
banner8.SS.leaveApprovalsURL = "bwpktais.P_SelectLeaveReportRoll"
banner8.SS.leaveApprovals8xLinkAvailable = 'Y'
banner8.SS.campusDirectoryURL = "bwpkedir.P_DisplayDirectory"
banner8.SS.campusDirectory8xLinkAvailable = 'Y'
banner8.SS.employeeMenuURL = "twbkwbis.P_GenMenu?name=pmenu.P_MainMnu"
banner8.SS.employeeMenuLinkAvailable = 'N'


//Tax links HR Dashboard
banner8.SS.electronicW2ConsentURL = "bwpkxtxs.P_W2Consent"
banner8.SS.electronicW2Consent8xLinkAvailable = 'Y'
banner8.SS.w2WageTaxStatementURL = "bwpkxtxs.P_ChooseW2Key"
banner8.SS.w2WageTaxStatement8xLinkAvailable = 'Y'
banner8.SS.w2CorrectedWageTaxStatementURL = "bwpkxtxs.P_ChooseW2cKey"
banner8.SS.w2CorrectedWageTaxStatement8xLinkAvailable = 'Y'
banner8.SS.w4EmployeeWithholdingAllowanceCertificateURL = "bwpkxtxs.P_ViewW4"
banner8.SS.w4EmployeeWithholdingAllowanceCertificate8xLinkAvailable = 'Y'
banner8.SS.yearEnd1095CStatementLinkAvailable = 'Y'
banner8.SS.yearEnd1095CStatementURL = 'bwpkxtxs.P_Choose1095cKey'
banner8.SS.yearEnd1094ReceiptIDEntryLinkAvailable = 'Y'
banner8.SS.yearEnd1094ReceiptIDEntryURL = 'bwpkxtxs.P_Disp1094ReceiptIDs'


banner8.SS.directDepositAllocationURL = "bwpkhpay.P_ViewDirectDeposit"
banner8.SS.directDepositAllocation8xLinkAvailable = 'Y'

//Tax links Canadian
banner8.SS.taxReturnURL = "bwvkxtax.P_SelectAdminOption"
banner8.SS.taxReturn8xLinkAvailable = 'N'
banner8.SS.electronicTaxFormsConsentURL = "bwvkecns.P_TaxFormsConsent"
banner8.SS.electronicTaxFormsConsent8xLinkAvailable = 'N'
banner8.SS.personalTaxCreditsSourceDeductionsURL = "bwvktd1a.P_TD1MainPage"
banner8.SS.personalTaxCreditsSourceDeductions8xLinkAvailable = 'N'

//Open Enrollment
banner8.SS.openEnrollmentAdminURL = "bwpkduti.f_disp_BENADMIN_link"
banner8.SS.openEnrollmentAdmin8xLinkAvailable = 'Y'
banner8.SS.openEnrollmentNewHireURL = "bwpkdsta.P_ShowEnrollmentMenu"
banner8.SS.openEnrollmentNewHire8xLinkAvailable = 'Y'
banner8.SS.openEnrollmentURL = "bwpkdsta.P_ShowEnrollmentMenu"
banner8.SS.openEnrollment8xLinkAvailable = 'Y'
banner8.SS.lifeEventChangeURL = "bwpkdsta.P_ShowEnrollmentMenu"
banner8.SS.lifeEventChange8xLinkAvailable = 'Y'

//Position Description Link
banner.SS.positionDescriptionLinkAvailable = 'Y'

//Direct Deposit Application Link.  NOTE - please disable the 8x direct deposit link if this one is being enabled.
// Use Consolidated Banner General SSB App Name if Consolidated General App is available, else use standalone
// DirectDeposit application URL
banner.SS.directDepositURL = 'http://<host_name>:<port_number>/<DirectDepositAppName>/'
banner.SS.directDepositAppLinkAvailable = 'N'


//Personal Info Application Link.
banner.SS.personalInfoURL = 'http://<host_name>:<port_number>/<GeneralAppName>/ssb/personalInformation#/personalInformationMain'
banner.SS.personalInfoAppLinkAvailable = 'Y'

//Deductions History Link.
banner.SS.deductionsHistoryLinkAvailable = 'Y'


//Labor Redistribution Application Link.  NOTE - please disable the 8x labor Redistribution link if this one is being enabled.
banner.SS.laborRedistributionAppLinkAvailable = 'Y'

//Effort Reporting Application Link.  NOTE - please disable the 8x effort Reporting link if this one is being enabled.
banner.SS.effortReportingAppLinkAvailable = 'Y'

//Use preferred first name through the application
ess.display.preferredFirstName = 'Y'

//Banner display components.
ess.displayComponent.Photo = 'Y'
ess.displayComponent.EmployeeStatus = 'Y'
ess.displayComponent.HomeOrganization  = 'Y'
ess.displayComponent.DistOrganization  = 'Y'
ess.displayComponent.Ecls  = 'Y'
ess.displayComponent.PartTimeFullTimeIndicator  = 'Y'
ess.displayComponent.OriginalHire = 'Y'
ess.displayComponent.CurrentHire  = 'Y'
ess.displayComponent.AdjustedHire  = 'Y'
ess.displayComponent.Seniority  = 'Y'
ess.displayComponent.FirstWorkDay  = 'Y'
ess.displayComponent.JobLocation = 'Y'
ess.displayComponent.College  = 'Y'
ess.displayComponent.Campus = 'Y'
ess.displayComponent.DistrictOrDivision  = 'Y'
ess.displayComponent.PositionSuffix = 'Y'
ess.displayComponent.Supervisor    = 'Y'
ess.displayComponent.TimeSheetOrgn  = 'Y'
ess.displayComponent.YtdEarnings   = 'Y'
ess.displayComponent.BenefitsSection = 'Y'
ess.displayComponent.BeneficiaryLink = 'Y'
ess.displayComponent.TaxSection = 'Y'
ess.displayComponent.PaySection = 'Y'
ess.displayComponent.EarningsSection = 'Y'
ess.displayComponent.EmployeeSummarySection = 'Y'
ess.displayComponent.PayStubPdf = 'Y'
ess.displayComponent.JobSummary = 'Y'

//Banner display Profile
ess.displayComponent.BirthDate  = 'Y'
ess.displayComponent.HireDate  = 'Y'

/**  END of Employee Profile Conigurations **/


/******************************************************************************
*Configuration to use themes served by the Theme Server*                                                                              *
***************************************************************************** **/
banner.theme.url = "<UPDATE_ME>" //Required only if theme server is remote. References the URL to the application hosting the Theme Server Example : . http://hostname:port/EmployeeSelfService/ssb/theme
banner.theme.name = "<UPDATE_ME>" // This is the desired theme name to use. In a MEP environment, the application uses the MEP code as the theme name instead of the banner.theme.name . A theme by this name must be created in the Theme Editor on the server specified by banner.theme.url
banner.theme.template = "EmployeeSelfService" // This is the name of the scss file containing the theme settings.
                                              // EmployeeSelfService.war includes a template named “employeeselfservice.scss” at css/theme/employeeselfservice.scss.
                                              //This file needs to be installed in the Theme Server’. Example : "employeeselfservice"
banner.theme.cacheTimeOut=120 // seconds, required only if the app is theme server

/** *****************************************************************************
 *                                                                              *
 *                          Google Analytics          *
 *                                                                              *
 ***************************************************************************** **/
banner.analytics.trackerId=
banner.analytics.allowEllucianTracker=true


/** ***************************************************************************
 *               Web Application Extensibility                                  *
 *******************************************************************************/

webAppExtensibility {
    locations {
       extensions = "path to the directory location where extensions JSON files will be written to and read from"
       resources = "path to the directory location where i18n files will be written to and read from"
    }
    adminRoles = "ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"
}

/******************************************************************************
 *                   Hibernate Secondary Level Caching                         *
 *******************************************************************************/
hibernate.cache.use_second_level_cache=true  // Default true. Make it false for Institution which is MEP enabled for HR or POSNCTL modules
hibernate.cache.use_query_cache=true         // Default true. Make it false for Institution which is MEP enabled for HR or POSNCTL modules



/** *****************************************************************************
 *                                                                              *
 *           Home Page link when error happens during authentication.           *
 *                                                                              *
 ***************************************************************************** **/
grails.plugin.springsecurity.homePageUrl='http://URL:PORT/'

/** *****************************************************************************
 *                                                                              *
 *                 Position Description  configuration                          *
 *                                                                              *
 *                    ++++BDM Configurations ++++                               *
******************************************************************************* **/
bdm.enabled = false
bdm.file.location = "<UPDATE ME>"
bdmserver {
                 AXWebServicesUrl 	= 'http://<UPDATE ME>/appxtender/axservicesinterface.asmx'
                 AXWebAccessURL  	= 'http://<UPDATE ME>/appxtender/'
                 Username       	= '<UPDATE ME>'
                 BdmDataSource     	= '<UPDATE ME>'
                 Password           = ''            //Leave this password as empty String.
                 AppName         	= '<UPDATE ME>'
            }

/** *************************************************************************************
*                                                                                       *
*                    ++++CornerStone Configurations ++++                                *
************************************************************************************** **/
csod.base.dir = "Update the folder location where CSOD export files can be created before downloading"
csod.file.delimiter = "|"

/** *************************************************************************************
*                                                                                       *
*                    ++++Quartz scheduler Configurations ++++                           *
************************************************************************************** **/
configJob.delay=60000   //Time in milliseconds to configure when the quartz scheduler should start after the server startup, if its not configured then the default value is 60000.
configJob.interval=60000 //Time in milliseconds to configure the interval at which the quartz schedule should run, default value is 60000 if not configured.
configJob.actualCount=-1 //The count of number of times the config job would run.  If value is -1, the job will run indefinitely.  If the value is o, the job will not run.  Default value is -1 when not configured.


/* Here are 3 patterns to use to configure the keys

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

*/

//Down below are keys configured using pattern 2
ssconfig.app.seeddata.keys = [
['ssbPassword.reset.enabled'],
['defaultWebSessionTimeout'],
['banner.picturesPath'],
['banner.hr.employerLogoPath'],
['banner.pdf.fopBaseUrl'],
['banner8.SS.url'],
['banner8.SS.addressUpdateURL'],
['banner8.SS.addressUpdate'],
['banner8.SS.emailUpdateURL'],
['banner8.SS.emailUpdate'],
['banner8.SS.emergencyContactUpdateURL'],
['banner8.SS.emergencyContactUpdate'],
['banner8.SS.benefitsEnrollmentURL'],
['banner8.SS.benefitsEnrollment8xLinkAvailable'],
['banner8.SS.currentBeneficiariesURL'],
['banner8.SS.currentBeneficiaries8xLinkAvailable'],
['banner8.SS.currentSummaryURL'],
['banner8.SS.currentSummary8xLinkAvailable'],
['banner8.SS.enterTimeURL'],
['banner8.SS.enterTime8xLinkAvailable'],
['banner8.SS.requestTimeOffURL'],
['banner8.SS.requestTimeOff8xLinkAvailable'],
['banner8.SS.electronicPersonalActionFormsURL'],
['banner8.SS.facultyLoadCompensationURL'],
['banner8.SS.facultyLoadCompensation8xLinkAvailable'],
['banner8.SS.salaryPlannerURL'],
['banner8.SS.salaryPlanner8xLinkAvailable'],
['banner8.SS.effortReportingURL'],
['banner8.SS.effortReporting8xLinkAvailable'],
['banner8.SS.laborRedistributionURL'],
['banner8.SS.laborRedistribution8xLinkAvailable'],
['banner8.SS.benefitsAdministratorURL'],
['banner8.SS.benefitsAdministrator8xLinkAvailable'],
['banner8.SS.morePersonalInfoUpdateURL'],
['banner8.SS.morePersonalInfoUpdate'],
['banner8.SS.timeSheetURL'],
['banner8.SS.timeSheet8xLinkAvailable'],
['banner8.SS.leaveApprovalsURL'],
['banner8.SS.leaveApprovals8xLinkAvailable'],
['banner8.SS.campusDirectoryURL'],
['banner8.SS.campusDirectory8xLinkAvailable'],
['banner8.SS.employeeMenuURL'],
['banner8.SS.employeeMenuLinkAvailable'],
['banner8.SS.electronicW2ConsentURL'],
['banner8.SS.electronicW2Consent8xLinkAvailable'],
['banner8.SS.w2WageTaxStatementURL'],
['banner8.SS.w2WageTaxStatement8xLinkAvailable'],
['banner8.SS.w2CorrectedWageTaxStatementURL'],
['banner8.SS.yearEnd1095CStatementLinkAvailable'],
['banner8.SS.yearEnd1095CStatementURL'],
['banner8.SS.yearEnd1094ReceiptIDEntryLinkAvailable'],
['banner8.SS.yearEnd1094ReceiptIDEntryURL'],
['banner8.SS.directDepositAllocationURL'],
['banner8.SS.directDepositAllocation8xLinkAvailable'],
['banner8.SS.taxReturnURL'],
['banner8.SS.taxReturn8xLinkAvailable'],
['banner8.SS.electronicTaxFormsConsentURL'],
['banner8.SS.personalTaxCreditsSourceDeductionsURL'],
['banner8.SS.openEnrollmentAdminURL'],
['banner8.SS.openEnrollmentAdmin8xLinkAvailable'],
['banner8.SS.openEnrollmentNewHireURL'],
['banner8.SS.openEnrollmentNewHire8xLinkAvailable'],
['banner8.SS.openEnrollmentURL'],
['banner8.SS.openEnrollment8xLinkAvailable'],
['banner8.SS.lifeEventChangeURL'],
['banner8.SS.lifeEventChange8xLinkAvailable'],
['banner.SS.directDepositURL'],
['banner.SS.directDepositAppLinkAvailable'],
['banner.SS.personalInfoURL'],
['banner.SS.personalInfoAppLinkAvailable'],
['banner.SS.deductionsHistoryLinkAvailable'],
['ess.display.preferredFirstName'],
['ess.displayComponent.Photo'],
['ess.displayComponent.EmployeeStatus'],
['ess.displayComponent.HomeOrganization'],
['ess.displayComponent.DistOrganization'],
['ess.displayComponent.Ecls'],
['ess.displayComponent.PartTimeFullTimeIndicator'],
['ess.displayComponent.OriginalHire'],
['ess.displayComponent.CurrentHire'],
['ess.displayComponent.AdjustedHire'],
['ess.displayComponent.Seniority'],
['ess.displayComponent.FirstWorkDay'],
['ess.displayComponent.JobLocation'],
['ess.displayComponent.College'],
['ess.displayComponent.Campus'],
['ess.displayComponent.DistrictOrDivision'],
['ess.displayComponent.PositionSuffix'],
['ess.displayComponent.Supervisor'],
['ess.displayComponent.TimeSheetOrgn'],
['ess.displayComponent.YtdEarnings'],
['ess.displayComponent.BenefitsSection'],
['ess.displayComponent.BeneficiaryLink'],
['ess.displayComponent.TaxSection'],
['ess.displayComponent.PaySection'],
['ess.displayComponent.EarningsSection'],
['ess.displayComponent.EmployeeSummarySection'],
['ess.displayComponent.PayStubPdf'],
['ess.displayComponent.JobSummary'],
['ess.displayComponent.BirthDate'],
['ess.displayComponent.HireDate'],
['banner.theme.url'],
['banner.theme.name'],
['banner.theme.template'],
['banner.theme.cacheTimeOut'],
['banner.analytics.trackerId'],
['banner.analytics.allowEllucianTracker'],
['csod.base.dir'],
['csod.file.delimiter'],
]

/**
* make sure following properties are included as Global SeedData key for DB Configurations
*/
ssconfig.global.seeddata.keys = [
['bdm.enabled'],
['bdm.file.location'],
['bdmserver.AXWebServicesUrl'],
['bdmserver.AXWebAccessURL'],
['bdmserver.Username'],
['bdmserver.BdmDataSource'],
['bdmserver.AppName'],
['bdmserver.Password'],
]
