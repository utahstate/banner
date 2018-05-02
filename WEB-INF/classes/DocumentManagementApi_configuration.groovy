/** *****************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

/** ****************************************************************************
 *                                                                              *
 *         Document Management API Configuration                                *
 *                                                                              *
 ***************************************************************************** **/ 
 
/** ****************************************************************************

This file contains configuration needed by the Banner Document Management API 9
web application. Please refer to the installation guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

	* BDM/ApplicationXtender Configuration
    
	* JMX Bean Names

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.
***************************************************************************** **/
 
 /** ****************************************************************************
 *                                                                              *
 *          BDM/ApplicationXtender(AX) Configuration         					*
 *                                                                              *
 ***************************************************************************** **/
 
bdmserver.file.location = (System.getenv('BDMSERVER_FILE_LOCATION') ?: '/tmp/upload')
bdmserver.AXWebServicesUrl = (System.getenv('BDMSERVER_AXWEBSERVICEURL') ?: 'http://axserver/appxtender/axservicesinterface.asmx')
bdmserver.AXWebAccessURL = (System.getenv('BDMSERVER_AXWEBACCESSURL') ?: 'http://axserver/appxtender/')
bdmserver.Username = (System.getenv('BDMSERVER_USERNAME') ?: 'BDMAPI_SUPERUSER')
bdmserver.Password = (System.getenv('BDMSERVER_PASSWORD') ?: 'MYPASSWORD')
bdmserver.DataSource = (System.getenv('BDMSERVER_DATASOURCE') ?: 'TEST')
bdmserver.BdmDataSource = (System.getenv('BDMSERVER_BDMDATASOURCE') ?: 'TEST')
bdmserver.KeyPassword = (System.getenv('BDMSERVER_KEYPASSWORD') ?: 'PASSWORD')


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
        log4j = "DocumentManagementApi-log4j"
    }
}

 
 /** ****************************************************************************
 *                                                                             *
 *                        Logging Configuration                                *
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
    def String loggingFileDir  =  "/usr/local/tomcat/logs"
    def String logAppName      = "DocumentManagementApi"
    def String loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()
    appenders {
        rollingFile name:'appLog', file:loggingFileName, maxFileSize:"${10*1024*1024}", maxBackupIndex:10, layout:pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch( grails.util.Environment.current.name.toString() ) {
        case 'development':
            root {
                debug 'stdout','appLog'
                additivity = true
            }
            info  'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            debug 'net.hedtech.banner.imaging'
            debug 'net.hedtech.bdm'
            break
        case 'test':
            root {
                error 'stdout','appLog'
                additivity = true
            }
            break
        case 'production':
            root {
                error 'stdout','appLog'
                additivity = true
            }
            info  'net.hedtech.banner.configuration.ApplicationConfigurationUtils'            
            error 'grails.app.service'
            error 'grails.app.controller'
            info 'net.hedtech.banner.representations'
            info 'net.hedtech.banner.supplemental.SupplementalDataService'
            error 'net.hedtech.banner.imaging'
            error 'net.hedtech.bdm'
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
	
	//addded by Manjuanth - Start
	off  'grails.app.controllers.net.hedtech.restfulapi.RestfulApiController'
	off  'grails.app.services'
	off  'grails.plugin.webxml'
	on     'net.hedtech.banner.general.overall.ldm'
	//addded by Manjuanth - End

	off  'org.apache.catalina'
	off  'org.apache.catalina.core'
	off  'org.apache.catalina.connector'
	off  'org.apache.catalina.util'
	off  'org.apache.catalina.startup'
	off  'org.apache.catalina.session'
	off  'org.apache.tomcat.util.digester'
	off  'org.apache.tomcat'
	off  'org.apache.tomcat.util.modeler'
	off  'org.apache.naming'
	off  'CacheHeadersGrailsPlugin'
	off  'org.codehaus.groovy.grails.web.context'
	off  'grails.app.resourceMappers.org.grails.plugin.resource'
	off  'org.codehaus.groovy.grails.web.converters.configuration'
	off  'net.sf.ehcache.hibernate'
	off  'org.codehaus.groovy.grails.context.support'
	off  'net.hedtech.banner.mep'
	off  'org.codehaus.groovy.grails.aop.framework.autoproxy'
	off  'org.codehaus.groovy.grails.web.filters'
	off  'org.apache.log4j.jmx'
	off  'grails.util'
	off  'grails.spring'
	off  'BannerCoreGrailsPlugin'
	off  'net.sf.ehcache.hibernate'
	off  'net.sf.ehcache.config'
	off  'net.sf.ehcache'
	off  'net.sf.ehcache.util'
	off  'org.codehaus.groovy.grails.validation'
	off  'org.apache.naming'
	off  'BootStrap'
	off  'grails.app.resourceMappers.org.grails.plugin.resource'
	off  'org.apache.coyote.http11'
	
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
}	  

/*******************************************************************************
 *                                                                             *
 *                                                                             *
 *                        Database BANPROXY settings                           *
 *                                                                             *
 *******************************************************************************/
// This setting sends all API database requests through the slower bannerDataSource.
// It is highly recommended for API performance that this be disabled.
// Disabling this setting does mean all audit user trails will be the username
// configured for your bannerSsbDataSource.

apiOracleUsersProxied=false

/*******************************************************************************
 *                                                                             *
 *                                                                             *
 *                        CORS (Cross Origin Resource Support)                 *
 *                                                                             *
 *******************************************************************************/
// These settings configure CORS support if required.
// By default browsers will not allow JavaScript or Java applets to
// retrieve resources (call these APIs) outside of the origin domain.
//
// The CORS plugin sets the header to disable the browser security check for
// domains matching the cors.allow.origin.regex pattern.  It's not recommended
// to open the pattern up as external sites could query your APIs and X-Site
// security would be compromised.

// Cors is disabled by default, set to true to enable.

cors.enabled = false

// Regex pattern for allowed origins.  If the origin supplied by the browser matches,
// then the CORS plugin will echo back the received Origin in the Allowed origin field
// of the response.
// Ex: cors.allow.origin.regex = '.*\\.ellucian\\.com'

cors.allow.origin.regex = '.*\\.<instutitution>\\.<com>'
