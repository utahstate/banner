/** ****************************************************************************
         Copyright 2020-2021 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/******************************************************************************
       Banner XE Recruiter Integration Manager App Configuration
 *******************************************************************************/

/******************************************************************************

This file contains configuration needed by the Banner XE Recruiter Integration Manager
application. Please refer to the Installation guide for additional information
regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * BRIM Configuration

    * JMX Bean Names

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

    * CAS SSO Configuration (supporting administrative and self service users)

*******************************************************************************/


/*****************************************************************************

                            BRIM Configuration
 *******************************************************************************/

/********************************************************************
* Below BRIM configuration properties must not be moved to database *
* Configuration properties requires for start the application       *
*********************************************************************/

//jms.jndi.environment.url="t3://mycollege.edu:8000"

shutdown.jms.listener.for.error.threshold = 99

shutdown.jms.listener.for.redelivery.threshold = 5

recruiter.http.connect.timeout=10000
recruiter.http.read.timeout=60000

// allow multiple configurations
brim.allowMultipleConfigurations=false

/*******************************************************************************
*                                                                              *
*                Application Server Configuration                              *
* When deployed to Tomcat, targetServer="tomcat"                               *
* When deployed to WebLogic, targetServer="weblogic"                           *
*                                                                              *
*******************************************************************************/
targetServer="tomcat"

/*******************************************************************************
*                                                                              *
*                Application Server Configuration                              *
* When connect to RabbitMQ/AMQP message broker, deployment.jms=false           *
* When connect to JMS message broker, deployment.jms=true                      *
*                                                                              *
*******************************************************************************/
deployment.jms=false

/** ******************************************************************************* *
*                                                                                   *
*                Application Server Configuration                                   *
* When deployed to Tomcat or WebLogic, and using AMQP/Rabbitmq message broker       *
* Uncomment the below line of code for rabbitmq otherwise leave as it is.           *
*************************************************************************************/

rabbitmq {
    connectionfactory {
        username                                        = (System.getenv('RABBITMQ_USERNAME') ?: "rabbitmqAdm")
        password                                        = (System.getenv('RABBITMQ_PASSWORD') ?: "#UPDATEME#")
        hostname                                        = (System.getenv('RABBITMQ_HOST') ?: "rabbitmqHost")
        port                                            = (System.getenv('RABBITMQ_PORT') ?: "5672")
        queueName                                       = (System.getenv('RABBITMQ_EXCHANGENAME') ?:"bep_events_topic")
        channelCacheSize                                = 1
        virtualHost                                     = (System.getenv('RABBITMQ_VIRTUALHOSTNAME') ?: "bep_events_host")
        enableSSL                                       = (System.getenv('RABBITMQ_ENABLESSL') ?: "false")
        //Put an actual path to a file starting with "file:" otherwise leave the value as NO_FILE
        keyStoreFileName = "NO_FILE"
        keyStorePassPhrase = ""

        //Put an actual path to a file starting with "file:" otherwise leave the value as NO_FILE
        trustStoreFileName = "NO_FILE"
        trustStorePassPhrase = ""
        sslAlgorithm                                    = 'TLSv1.2'
    }
       concurrentConsumers                              =  1
       maxConcurrentConsumers                           =  1
       channelTransacted                                =  true
       defaultRequeueRejected                           =  true
       autoStartup                                      =  false
       acknowledgeMode                                  =  org.springframework.amqp.core.AcknowledgeMode.MANUAL
}

/**************** Below  messageTypeConfig is use for enabling and consuming the events message as a consumer ******/
messageTypeConfig = [
        'RECRUITER_ERP_ID'                                   : [enabled:true,path:'/BannerUpdateERPID',supports:['INSERT','UPDATE']],
        'RECRUITER_APPLICATION_STATUS'                       : [enabled:true,path:'/BannerUpdateApplicationStatus',supports:['INSERT','UPDATE','DELETE']],
        'RECRUITER_ADMIT_DATE_APPLACCEPT'                    : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ADMIT_DATE_INSTACCEPT'                    : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ENROLLED_DATE_REGISTERED'                 : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT','UPDATE']],
        'RECRUITER_CONFIRMED_DATE_REGISTERED'                : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ENROLLED_DATE_APPLACCEPT'                 : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ENROLLED_DATE_DECISIONCODE'               : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_CONFIRMED_DATE_DECISIONCODE'              : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_BDM_DOCUMENT'                             : [enabled:true,path:'/GetRecruiterID',supports:['INSERT']],
        'RECRUITER_CONFIRMED_DATE_DEPOSITPAID'               : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_UPDATE_SUPPLEMENT_ITEMS'                  : [enabled:true,path:'/UpdateSupplement',supports:['INSERT']],
        'RECRUITER_APPLICATION_HISTORY'                      : [enabled:true,path:'/UpdateUcasApplicationHistory',supports:['UPDATE']],
        'RECRUITER_GOBUMAP_UDC_ID'                           : [enabled:true,path:'/BannerUpdateERPID',supports:['UPDATE']]
]

/* BANNER AUTHENTICATION PROVIDER CONFIGURATION */
banner {
        sso {
                authenticationProvider = 'default' //  Valid values are: 'default', 'cas' (must regenerate WAR file when changed)
                authenticationAssertionAttribute = 'UDC_IDENTIFIER'
        }
}

grails {
    plugin {
        springsecurity {
            cas {
                active = false
                if (active){
                    grails.plugin.springsecurity.providerNames = ['casBannerAuthenticationProvider', 'selfServiceBannerAuthenticationProvider', 'bannerAuthenticationProvider']
                }
                serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl = (System.getenv('BRIM_URL') ?: 'http://BANNER9_HOST:PORT') +'/brim/login/cas'
                serverName = (System.getenv('BRIM_URL') ?: 'http://BANNER9_HOST:PORT') 
                proxyCallbackUrl = (System.getenv('BRIM_URL') ?: 'http://BANNER9_HOST:PORT') + '/brim/secure/receptor'
                loginUri = '/login'
                sendRenew = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                serverUrlEncoding = 'UTF-8'
                filterProcessesUrl = '/login/cas'
                if (useSingleSignout) {
                    grails.plugin.springsecurity.useSessionFixationPrevention = false
                }
            }
            logout {
                afterLogoutUrl = 'http://CAS_HOST:CAS_PORT/brim/'
            }
        }
    }
}

/** **********************************************************************************
 *                                                                                   *
 *                      ConfigJob (Platform 9.23)                                    *
 *     Support for configurations to reside in the database.                         *
 * Properties to set the interval and the number of times the config job would run   *
 * for ConfigJob.groovy i.e. the job scheduled to update the configuration                   *
 * properties from DB. We recommend configuring interval of the configJob in         *
 * such a way that it does not run as often, to help improve performance.            *
 *                                                                                   *
 ********************************************************************************** **/

configJob {
        interval = 86400000 // 24 hours
        actualCount = -1

}
banner.applicationName="brim"
