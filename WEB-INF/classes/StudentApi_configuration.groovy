/** *****************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

/** ****************************************************************************
 *                                                                              *
 *         Student API Configuration                                            *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

This file contains configuration needed by the Student API Banner 9
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.

This configuration file contains the following sections:

	* JMX Bean Names

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

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
        log4j = "StudentApi-log4j"
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
    def String loggingFileDir  =  '/usr/local/tomcat'
    def String logAppName      = "StudentApi"
    def String loggingFileName = "${loggingFileDir}/logs/${logAppName}.log".toString()
    appenders {
        rollingFile name:'appLog', file:loggingFileName, maxFileSize:"${10*1024*1024}", maxBackupIndex:10, layout:pattern( conversionPattern: '%d{[EEE, dd-MMM-yyyy @ HH:mm:ss.SSS]} [%t] %-5p %c %x - %m%n' )
    }

    switch( grails.util.Environment.current.name.toString() ) {
        case 'development':
            root {
                error 'stdout'
                additivity = true
            }
            info  'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
            error 'net.hedtech.banner.representations'
            error 'net.hedtech.banner.supplemental.SupplementalDataService'
            break
        case 'test':
            root {
                error 'stdout'
                additivity = true
            }
            break
        case 'production':
            root {
                error 'appLog'
                additivity = true
            }
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
    off 'grails.plugins.springsecurity'
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
    // debug 'grails.app.<artefactType>.ClassName
	off 'grails.app.controllers.net.hedtech.restfulapi.RestfulApiController'
	off 'grails.app.services'
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

apiOracleUsersProxied=(System.getenv('APIORACLEUSERSPROXIED') ? Boolean.parseBoolean(System.getenv('APIORACLEUSERSPROXIED')) : false)

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

cors.enabled = (System.getenv('CORS_ENABLED') ? Boolean.parseBoolean(System.getenv('CORS_ENABLED')) : false)

// Regex pattern for allowed origins.  If the origin supplied by the browser matches,
// then the CORS plugin will echo back the received Origin in the Allowed origin field
// of the response.
// Ex: cors.allow.origin.regex = '.*\\.ellucian\\.com'

cors.allow.origin.regex = '.*\\.<instutitution>\\.<com>'

/**************************************************************************************************
                                     Override "max" upper limit

LIST APIs support pagination with "max" and "offset" parameters.
API service apply two rules on page size paramerter "max".  First is "default" and second is "upper limit".
If LIST request comes without "max", it will be set to "default".
If LIST request comes with very high "max", it will be reset to "upper limit".
The values for "default" and "upper limit" varies from API to API and are decided based on performance of API.

In few scenarios, like Bulk load, clients want to fetch more records with each LIST request.
For that, it is necessary to override programmatic "max upper limit" with the configuration setting api.<resource name>.page.maxUpperLimit
Note: It might also be necessary to increase transaction timeout to fetch more records.
***************************************************************************************************/
//api.courses.page.maxUpperLimit=1000
//api.sections.page.maxUpperLimit=1000
//api."instructional-events".page.maxUpperLimit=1000
