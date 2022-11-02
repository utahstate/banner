/*******************************************************************************
Copyright 2017-2020 Ellucian Company L.P. and its affiliates.
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
'banner_configuration.example' file.

*******************************************************************************/

/*******************************************************************************
*                                                                              *
*              Banner Event Publisher DataSource Configuration                  *
*                                                                              *
*******************************************************************************/

dataSource_cdcadmin {
    // JNDI configuration for use in 'production' environment
    jndiName = "jdbc/cdcadmin"
    transactional = false
}

dataSource_events {
    // JNDI configuration for use in 'production' environment
    jndiName = "jdbc/events"
    transactional = false
}

bep {
	// App Server
	// Possible vaues are either "TOMCAT" or "WEBLOGIC"
	app.server = "TOMCAT"

	// Message Broker
	// Possible vaues are "RABBITMQ" or "WEBLOGIC" or "RABBITMQ/WEBLOGIC"
	message.broker = "RABBITMQ"

	//Retry interval to publish to broker in SECONDS
	publish.retry.interval = 45
}

//RabbitMQ configuration
rabbitmq {
	host = (System.getenv('RABBITMQ_HOST') ?: "rabbitmqHost")
	port = "5672"
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
//                       +++ Self Service Support +++
//
// ******************************************************************************

ssbEnabled = (System.getenv('SSBENABLED') ? Boolean.parseBoolean(System.getenv('SSBENABLED')) : true )
ssbOracleUsersProxied = (System.getenv('SSBORACLEUSERSPROXIED') ? Boolean.parseBoolean(System.getenv('SSBORACLEUSERSPROXIED')) : true )

/* BANNER AUTHENTICATION PROVIDER CONFIGURATION */
banner {
	sso {
		authenticationProvider = 'default'
		authenticationAssertionAttribute = 'UDC_IDENTIFIER'
	}
}

/* CAS & SAML Configuration to be disabled */
grails.plugin.springsecurity.saml.active = false
grails.plugin.springsecurity.cas.active = false

/* Banner Logout URL Configurations */
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

/********************************************************************************
*                                                                               *
* AboutInfoAccessRoles will help to check whether user is allowed to view       *
* the platform version                                                          *
*                                                                               *
******************************************************************************* **/
aboutInfoAccessRoles = ["ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M"]

/********************************************************************************
*                     Application Server Configuration                          *
* When deployed on Tomcat this configuration should be targetServer=“tomcat”    *
* When deployed on Weblogic this configuration should be targetServer=“weblogic”*
*********************************************************************************/
targetServer="tomcat"

configJob {
	interval = 86400000 // 24 hours
	actualCount = -1
}
