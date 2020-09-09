/** ****************************************************************************
         Copyright 2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/** ****************************************************************************
 *       Banner XE Recruiter Integration Manager App Configuration
 *******************************************************************************/

import org.apache.log4j.DailyRollingFileAppender

/** ****************************************************************************

This file contains configuration needed by the Banner XE Recruiter Integration Manager
application. Please refer to the Installation guide for additional information
regarding the configuration items contained within this file.

This configuration file contains the following sections:

    * BRIM Configuration

    * JMX Bean Names

    * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

    * CAS SSO Configuration (supporting administrative and self service users)

***************************************************************************** **/


/* ****************************************************************************
 *                                                                             *
 *                            BRIM Configuration                               *
 *                                                                             *
 *******************************************************************************/

jms.jndi.environment.url="t3://mycollege.edu:8000"

shutdown.jms.listener.for.error.threshold = 99

shutdown.jms.listener.for.redelivery.threshold = 5

recruiter.http.connect.timeout=10000
recruiter.http.read.timeout=60000


// allow multiple configurations
brim.allowMultipleConfigurations=false


// compatability with older versions of CRM software
brim.compatibility.beforeRecruiter38=false


// provision applicant configuration
brim.customProperty.addressTypes="SC,MA"
brim.customProperty.homePhoneTypes="SC,MA"
brim.customProperty.cellPhoneTypes="CELL"
brim.customProperty.emailTypes="PERS"
brim.customProperty.domesticAddressNationCodes="US,CA"

// recruitment opportunity withdraw codes
brim.customProperty.withdrawCodes="W"

//accept decision codes
brim.customProperty.acceptDecisionCodes="25,35"

//confirm decision codes
brim.customProperty.confirmDecisionCodes="25,35"

// custom field support
//brim.customProperty.sqlProcessCode="BRIM"
//brim.customProperty.applicantSqlCode="BRIM_APPLICANT_DATA"
//brim.customProperty.applicantInterestsSqlCode="BRIM_INTERESTS_DATA"
//brim.customProperty.applicantTestScoresSqlCode="BRIM_TEST_SCORES_DATA"
//brim.customProperty.applicantHighSchoolsSqlCode="BRIM_HIGH_SCHOOLS_DATA"
//brim.customProperty.applicantPriorCollegesSqlCode="BRIM_PRIOR_COLLEGES_DATA"
//brim.customProperty.applicantItemsReceivedSqlCode="BRIM_ITEMS_RECEIVED_DATA"
//brim.customProperty.applicationSqlCode="BRIM_APPLICATION_DATA"
//brim.customProperty.applicationStatusSqlCode="BRIM_APPLICATION_STATUS_DATA"
//brim.customProperty.applicantFormQualificationsSqlCode="BRIM_UCAS_FORM_QUALS_DATA"
//brim.customProperty.applicantEmploymentDataSqlCode="BRIM_UCAS_EMPLOYMENT_DATA"
//brim.customProperty.applicantRefereeDataSqlCode="BRIM_UCAS_REFEREE_DATA"
//brim.customProperty.OfferCodeSqlCode="BRIM_UCAS_OFFER_DATA"
//brim.customProperty.applicantUcasHighSchoolsSqlCode ="BRIM_UCAS_SCHOOLS_DATA"
//brim.customProperty.applicantUcasPriorCollegesSqlCode ="BRIM_UCAS_COLLEGES_DATA"

//Make it true if Message Broker is JMS
//Make it false if Message Broker is Rabbitmq/AMQP
deployment.jms=false

//start Rabbit mq configuration
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

messageTypeConfig = [
        'RECRUITER_ERP_ID'                                   : [enabled:true,path:'/BannerUpdateERPID',supports:['INSERT']],
        'RECRUITER_APPLICATION_STATUS'                       : [enabled:true,path:'/BannerUpdateApplicationStatus',supports:['INSERT','UPDATE','DELETE']],
        'RECRUITER_ADMIT_DATE_APPLACCEPT'                    : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ADMIT_DATE_INSTACCEPT'                    : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ENROLLED_DATE_REGISTERED'                 : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_CONFIRMED_DATE_REGISTERED'                : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ENROLLED_DATE_APPLACCEPT'                 : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_ENROLLED_DATE_DECISIONCODE'               : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_CONFIRMED_DATE_DECISIONCODE'              : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_BDM_DOCUMENT'                             : [enabled:true,path:'/GetRecruiterID',supports:['INSERT']],
        'RECRUITER_CONFIRMED_DATE_DEPOSITPAID'               : [enabled:true,path:'/BannerUpdateApplication',supports:['INSERT']],
        'RECRUITER_UPDATE_SUPPLEMENT_ITEMS'                  : [enabled:true,path:'/UpdateSupplement',supports:['INSERT']],
        'RECRUITER_APPLICATION_HISTORY'                      : [enabled:true,path:'/UpdateUcasApplicationHistory',supports:['UPDATE']]
]


/* ****************************************************************************
 *                                                                             *
 *                              JMX Bean Names                                 *
 *                                                                             *
 *******************************************************************************/
// The names used to register Mbeans must be unique for all applications deployed
// into the JVM.  This configuration should be updated for each instance of each
// application to ensure uniqueness.
jmx {
    exported {
        log4j = "brim-log4j"
    }
}


/* ****************************************************************************
 *                                                                             *
 *                          Logging Configuration                              *
 *                                                                             *
 **************************************************************************** **/
// If we specify a 'logFileDir' as a system property, we'll write the file to that directory.
// Otherwise, we'll log to the target/logs directory.
String loggingFileDir = "/usr/local/tomcat/logs"
String loggingFileName = "${loggingFileDir}/brim.log".toString()
String eventFileName = "${loggingFileDir}/brim_event.log".toString()

log4j = {
    appenders {
        appender new DailyRollingFileAppender(name: 'appLog', datePattern: "'.'yyyy-MM-dd", fileName: loggingFileName, layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n') )
        appender new DailyRollingFileAppender(name: 'eventLog', datePattern: "'.'yyyy-MM-dd", fileName: eventFileName, layout: pattern(conversionPattern:'%d [%t] %-5p %c{2} %x - %m%n') )
    }
    root {
        error 'appLog'
    }
    info eventLog: 'eventLog', additivity: false

    info 'BepConsumerGrailsPlugin'
    info 'grails.app.services.net.hedtech.jms.container.manager.SpringDMLCManagerService'

    info 'grails.app.conf.BootStrap'

    warn 'grails.app.services.net.hedtech.r2b.loader.LoadService'

    // uncomment to debug Recruiter-To-Banner message processing
    //debug 'grails.app.services.net.hedtech.banner.about.AboutBrimService'
    //debug 'grails.app.services.net.hedtech.banner.general.BannerIdService'
    //debug 'grails.app.services.net.hedtech.banner.general.EnterpriseIdService'
    //debug 'grails.app.services.net.hedtech.banner.student.InvalidSurrogateIdService'
    //debug 'grails.app.services.net.hedtech.banner.student.admissions.ApplicantService'
    //debug 'grails.app.services.net.hedtech.banner.student.admissions.ApplicantSupplementalService'
    //debug 'grails.app.services.net.hedtech.banner.student.admissions.ApplicantApplicationIdService'
    //debug 'grails.app.services.net.hedtech.r2b.banner.applications.RecruitmentOpportunityService'
    //debug 'grails.app.services.net.hedtech.r2b.banner.applications.ApplicationStatusService'
    //debug 'net.hedtech.r2b.util.MessageServiceHolder'
    //debug 'grails.app.services.net.hedtech.r2b.dbmslock.DbmsLockService'
    //debug 'net.hedtech.r2b.loader.LoaderServiceBase'
    //debug 'grails.app.services.net.hedtech.r2b.loader.MatchService'
    //debug 'grails.app.services.net.hedtech.r2b.loader.PushService'
    //debug 'net.hedtech.r2b.request.RequestServiceBase'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterApplicationIdService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterApplicationStatusService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterConnectionTestService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterIdService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterProspectService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterTestScoreService'
    //debug 'grails.app.services.net.hedtech.banner.student.admissions.RecruiterChecklistService'
    //debug 'grails.app.services.net.hedtech.banner.student.recruiting.RecruitingItemsReceivedService'
    //debug 'grails.app.services.net.hedtech.r2b.banner.finaid.FinancialAidApplicationService'
    //debug 'grails.app.services.net.hedtech.r2b.banner.finaid.FinancialAidAwardService'
    //debug 'grails.app.services.net.hedtech.r2b.banner.finaid.FinancialAidNewProspectService'
    //debug 'grails.app.services.net.hedtech.r2b.banner.translations.EnterpriseIdTranslationService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterRejectStatusService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterOfferCodeService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterDecisionStatusService'
    //debug 'grails.app.services.net.hedtech.r2b.resource.RecruiterTransactionStatusService'

    // uncomment to trace BEP event internal processing
    //trace 'grails.app.services.net.hedtech.banner.command.CommandProcessorService'
    //trace 'grails.app.services.net.hedtech.banner.database.SqlExecutorService'
    //trace 'grails.app.services.net.hedtech.banner.event.ErrorLoggingService'
    //trace 'grails.app.services.net.hedtech.banner.event.history.EventHistoryService'
    //trace 'grails.app.services.net.hedtech.banner.parser.util.XmlXPathUtilityService'
    //trace 'grails.app.services.net.hedtech.banner.processor.MessageProcessorService'
    //trace 'grails.app.services.net.hedtech.banner.threshold.ThresholdManagerService'
    //trace 'grails.app.services.net.hedtech.jms.registry.RegistryService'
    //trace 'grails.app.services.net.hedtech.r2b.jms.event.response.EventDispatcherService'

    // uncomment to trace BEP event command processing
    //trace 'net.hedtech.jms.consumer.handlers.CommandBase'
    //trace 'net.hedtech.jms.pubsub.BannerMessageListener'
    //trace 'net.hedtech.rabbit.pubsub.BannerRabbitMessageListener'
    //trace 'net.hedtech.jms.pubsub.BannerMessagePublisher'
    //trace 'net.hedtech.r2b.jms.event.command.AdmitDateApplAcceptCommand'
    //trace 'net.hedtech.r2b.jms.event.command.AdmitDateInstAcceptCommand'
    //trace 'net.hedtech.r2b.jms.event.command.ApplicationStatusCommand'
    //trace 'net.hedtech.r2b.jms.event.command.ConfirmedDateDecisionCodeCommand'
    //trace 'net.hedtech.r2b.jms.event.command.ConfirmedDateDecisionCodeCommandDependencyAcceptedDate'
    //trace 'net.hedtech.r2b.jms.event.command.ConfirmedDateDepositPaidCommand'
    //trace 'net.hedtech.r2b.jms.event.command.ConfirmedDateDepositPaidCommandDependencyAcceptedDate'
    //trace 'net.hedtech.r2b.jms.event.command.ConfirmedDateRegisteredCommand'
    //trace 'net.hedtech.r2b.jms.event.command.ConfirmedDateRegisteredCommandDependencyAcceptedDate'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateApplAcceptCommand'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateApplAcceptCommandDependencyAcceptedDate'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateApplAcceptCommandDependencyConfirmedDate'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateDecisionCodeCommand'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateDecisionCodeCommandDependencyAcceptedDate'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateDecisionCodeCommandDependencyConfirmedDate'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateRegisteredCommand'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateRegisteredCommandDependencyAcceptedDate'
    //trace 'net.hedtech.r2b.jms.event.command.EnrolledDateRegisteredCommandDependencyConfirmedDate'
    //trace 'net.hedtech.r2b.jms.event.command.ErpIdAddedCommand'
    //trace 'net.hedtech.r2b.jms.event.command.BDMDocumentEventCommand'
    //trace 'net.hedtech.r2b.jms.event.command.UpdateSupplementItemsEventCommand'
    //trace 'net.hedtech.r2b.jms.event.command.ApplicationHistoryEventCommand'

    // uncomment to trace database connections and security authentication
    //trace 'net.hedtech.banner.configuration.ApplicationConfigurationUtils'
    //trace 'net.hedtech.banner.db'
    //trace 'net.hedtech.banner.db.BannerConnection'
    //trace 'net.hedtech.banner.db.BannerDs'
    //trace 'net.hedtech.banner.security'
    //trace 'net.hedtech.banner.security.AccessControlFilters'
    //trace 'net.hedtech.banner.security.BannerAccessDecisionVoter'
    //trace 'net.hedtech.banner.security.BannerAuthenticationProvider'
    //trace 'net.hedtech.banner.security.CasAuthenticationProvider'
    //trace 'net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider'
    //trace 'net.hedtech.banner.security.BannerGrantedAuthorityService'

    // uncomment to display RESTful API metrics
    //trace 'net.hedtech.banner.restfulapi.RestfulApiServiceMetrics'

    // uncomment to display hibernate sql and parameter bindings
    //debug 'org.hibernate.SQL'
    //trace 'org.hibernate.type.descriptor.sql.BasicBinder'

    // uncomment to display groovy sql in the log (also uncomment grails.logging.jul.usebridge = true below)
    //debug 'groovy.sql.Sql'

    // uncomment to trace CAS authentication
    //trace 'net.hedtech.banner.security'
    //debug 'org.springframework.security'
    //debug 'org.jasig'
}

// activate logging for groovy sql (also uncomment debug 'groovy.sql.Sql' in log4j configuration above)
//grails.logging.jul.usebridge = true


// ******************************************************************************
//
//                       +++ CAS CONFIGURATION +++
//
// ******************************************************************************
banner {
    sso {
        authenticationProvider           = 'cas' //  Valid values are: 'default', 'cas' (must regenerate WAR file when changed)
        authenticationAssertionAttribute = 'UDC_IDENTIFIER'
    }
}

grails {
    plugin {
        springsecurity {
            cas {
                active = true
                if (active) {
                  grails.plugin.springsecurity.provierNames = ['casBannerAuthenticationProvider','selfServiceBannerAuthenticationProvider','bannerAuthenticationProvider']
                }
                serverUrlPrefix  = (System.getenv('CAS_URL') ?: 'http://CAS_HOST:PORT/cas')
                serviceUrl       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/brim/j_spring_cas_security_check"
                serverName       = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT')
                proxyCallbackUrl = (System.getenv('BANNER9_URL') ?: 'http://BANNER9_HOST:PORT') + "/brim/secure/receptor"
                loginUri         = '/login'
                sendRenew        = false
                proxyReceptorUrl = '/secure/receptor'
                useSingleSignout = true
                key = 'grails-spring-security-cas'
                artifactParameter = 'SAMLart'
                serviceParameter = 'TARGET'
                filterProcessesUrl = '/j_spring_cas_security_check'
                serverUrlEncoding = 'UTF-8'
                if (useSingleSignout) {
                  grails.plugin.springsecurity.useSessionFixationPrevention =false
                }
            }
            logout {
              afterLogoutUrl = (System.getenv('BANNER9_AFTERLOGOUTURL') ?: 'https://cas-server/logout?url=http://myportal/main_page.html')
            }
//                active           = false
//                if (active) {
//                    grails.plugin.springsecurity.providerNames = ['casBannerAuthenticationProvider', 'selfServiceBannerAuthenticationProvider', 'bannerAuthenticationProvider']
//                }
//                serverUrlPrefix  = 'https://CAS_HOST:CAS_PORT/cas'
//                serviceUrl       = 'https://BRIM_HOST:BRIM_PORT/brim/j_spring_cas_security_check'
//                serverName       = 'https://BRIM_HOST:BRIM_PORT'
//                proxyCallbackUrl = 'https://BRIM_HOST:BRIM_PORT/brim/secure/receptor'
//                loginUri         = '/login'
//                sendRenew        = false
//                proxyReceptorUrl = '/secure/receptor'
//                useSingleSignout = true
//                key = 'grails-spring-security-cas'
//                artifactParameter = 'SAMLart'
//                serviceParameter = 'TARGET'
//                serverUrlEncoding = 'UTF-8'
//                filterProcessesUrl = '/j_spring_cas_security_check'
//                if (useSingleSignout) {
//                    grails.plugin.springsecurity.useSessionFixationPrevention = false
//                }
//            }
//            logout {
//                afterLogoutUrl = 'https://CAS_HOST:CAS_PORT/cas/logout?url=https://BRIM_HOST:BRIM_PORT/brim'
//            }
        }
    }
}
