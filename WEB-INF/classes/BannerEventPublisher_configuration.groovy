import org.apache.log4j.DailyRollingFileAppender

/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/** ****************************************************************************
 *                                                                              *
 *                Self-Service Banner 9 Event Configuration                     *
 *                                                                              *
 ***************************************************************************** **/

/******************************************************************************

 This file contains configuration needed by the Banner Event Publisher
 web application. Please refer to the administration guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:

 * Self Service Support

 * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

 * CAS SSO Configuration (supporting administrative and self service users)

 NOTE: DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.

 *******************************************************************************/

/*******************************************************************************
 *                                                                              *
 *              Banner Event Publisher DataSource Configuration                  *
 *                                                                              *
 *******************************************************************************/

dataSource_cdcadmin {
    //JNDI configuration for use in 'production' environment
	jndiName = "java:comp/env/jdbc/cdcadmin"
    transactional = false
}

dataSource_events {
    //JNDI configuration for use in 'production' environment
	jndiName = "java:comp/env/jdbc/events"
	transactional = false
}

dataSource_bannerSsbDataSource {
    //JNDI configuration for use in 'production' environment
	jndiName = "java:comp/env/jdbc/bannerSsbDataSource"
	transactional = false
}

bep {
	//App Server
	//Possible values are either "TOMCAT" or "WEBLOGIC"
    app.server = "TOMCAT"

	//Message Broker
	//Possible values are "RABBITMQ" or "WEBLOGIC" or "RABBITMQ/WEBLOGIC"
	message.broker = "RABBITMQ"

	//Retry interval to publish to broker in SECONDS
    publish.retry.interval = 45
}

//RabbitMQ configuration
rabbitmq {
	host = (System.getenv('RABBITMQ_HOST') ?: "rabbitmqHost")
	port = (System.getenv('RABBITMQ_PORT') ?: "5672")
	userName = (System.getenv('RABBITMQ_USERNAME') ?: "rabbitmqAdm")
	password = (System.getenv('RABBITMQ_PASSWORD') ?: "#UPDATEME#")
	virtualHostName = (System.getenv('RABBITMQ_VIRTUALHOSTNAME') ?: "bep_events_host")
	exchangeName = (System.getenv('RABBITMQ_EXCHANGENAME') ?:"bep_events_topic")
	enableSSL = (System.getenv('RABBITMQ_ENABLESSL') ?: "false")

	//Validate rabbit connections
    validate = true

	//Put an actual path to a file starting with "file:" otherwise leave the value as NO_FILE
	keyStoreFileName = "NO_FILE"
	keyStorePassPhrase = ""

	//Put an actual path to a file starting with "file:" otherwise leave the value as NO_FILE
	trustStoreFileName = "NO_FILE"
	trustStorePassPhrase = ""
}

jms {
	validate = true
}

// This configuration needs to be done in milliseconds for the footer to appear in the screen
footerFadeAwayTime=2000

// Application Navigator opens embedded applications within an iframe. To protect against the clickjacking vulnerability,
// integrating applications will have to set the X-Frame options to protect the "login/auth" URI from loading in the iframe and
// set it to denied mode. This setting is needed if the application needs to work inside Application Navigator and
// the secured application pages will be accessible as part of the single-sign on solution.
grails.plugin.xframeoptions.urlPattern = '/login/auth'
grails.plugin.xframeoptions.deny = true


// End of configuration


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
        log4j = "BannerEventPublisher-log4j"
    }
}

// ******************************************************************************
//
//                       +++ Self Service Support +++
//
// ******************************************************************************

ssbEnabled = (System.getenv('SSBENABLED') ? Boolean.parseBoolean(System.getenv('SSBENABLED')) : true )
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.parseBoolean(System.getenv('SSBORACLEUSERSPROXIED')) : true )

// BANNER AUTHENTICATION PROVIDER CONFIGURATION
banner {
    sso {
		authenticationProvider = 'default'
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
	}
}

// CAS & SAML Configuration to be disabled
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.cas.active = false

// Banner Logout URL Configurations
grails {
    plugin {
        springsecurity {
            logout {
                    afterLogoutUrl = '/'
                    mepErrorLogoutUrl = '/logout/logoutPage'
            }
        }
    }
}

// ******************************************************************************
//
//                       +++ LOGGER CONFIGURATION +++
//
// ******************************************************************************
String loggingFileDir =  "/usr/local/tomcat/logs"
String logAppName = "BannerEventPublisher"
String loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()
String eventFileName = "${loggingFileDir}/bep_event_summary.log".toString()

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
        appender new DailyRollingFileAppender(name: 'appLog', datePattern: "'.'yyyy-MM-dd", fileName: loggingFileName, layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n') )
        appender new DailyRollingFileAppender(name: 'eventLog', datePattern: "'.'yyyy-MM-dd", fileName: eventFileName, layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n') )
    }
    root {
        info 'stdout','appLog'
        additivity = true
    }
    info eventLog: 'eventLog', additivity: false

    info 'BannerEventPublisherGrailsPlugin'

    info 'com.sungardhe.eventing.listener.BEPContextListener'

    info 'BootStrap'

    info 'grails.app.services.net.hedtech.aq.container.manager.SpringDMLCManagerService'

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
    error 'org.codehaus.groovy.grails.plugins'            // plugins
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
    error 'net.hedtech.banner.db'
    error 'net.hedtech.banner.security.BannerAccessDecisionVoter'
    error 'net.hedtech.banner.security.BannerAuthenticationProvider'
    error 'net.hedtech.banner.security.CasAuthenticationProvider'
    error 'net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider'
    error 'grails.plugin.springsecurity'
    error 'org.springframework.security'
    error 'org.apache.http.headers'
    error 'org.apache.http.wire'

	error 'net.hedtech.banner.menu'
	error 'net.hedtech.banner.tools'
	info  'net.hedtech.tools'

    // Grails provides a convenience for enabling logging within artefacts, using 'grails.app.XXX'.
    // Unfortunately, this configuration is not effective when 'mixing in' methods that perform logging.
    // Therefore, for controllers and services it is recommended that you enable logging using the controller
    // or service class name (see above 'class name' based configurations).  For example:
    // all 'net.hedtech.banner.testing.FooController' // turns on all logging for the FooController
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
