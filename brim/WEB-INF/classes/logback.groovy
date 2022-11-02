/******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
/**
 *    This is the default logging configuration for the brim application.
 *
 *    Each Banner application will have its own logging file.  Example: brim.log
 *    Banner logging files are set up to roll on a daily basis with SizeAndTimeBasedRolling Policy. Example: brim-2020-03-05-0.log
 *
 *        Default settings for Banner logging:
 *           Root logger level: ERROR
 *           Logging is turned DEBUG for specific packages (see below) with commented. This should be uncomment based on requirement.
 *
 *        User can specify the different logging directory using the following system property:
 *            -Dbanner.logging.dir=<full directory path>  (THIS MUST BE AN ABSOLUTE PATH WITH WRITE PERMISSION)
 *            Example: -Dbanner.logging.dir=/home/tomcat/logs
 *
 *        If different logging directory is not configured then the log files will be generated in the build folder as Provided default by Grails.
 *
 *        Use JMX to change logger levels for ROOT or specific packages/artifacts.
 *
 *        Optionally, you can modify this file to change the logger level for ROOT or specific packages/artifacts.
 *
 *        Valid logger levels in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < OFF
 */
import ch.qos.logback.core.util.FileSize
import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import groovy.json.JsonOutput
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider
import net.logstash.logback.composite.loggingevent.*
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// This line is only required in logback.groovy which is present in /grails-app/conf.
// This should be commented in the external logback.groovy
ExternalConfigurationUtils.setupExternalLogbackConfig()

def encoderPattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%t] %-5p %c %X - %m%n"
def logAppName =  Metadata.current.getApplicationName()   // The application name for logging purposes.
// Set the logging output directory
def loggingFileDir = System.properties["banner.logging.dir"] ?: '/usr/local/tomcat/logs'
def loggingJsonFileName = "${loggingFileDir}/${logAppName}.json".toString()
def eventLoggingJsonFileName = "${loggingFileDir}/brim_event.json".toString()
def loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()
def eventLoggingFileName = "${loggingFileDir}/brim_event.log".toString()
def fileLoggingFormat = "JSON"
def timeStampPatternAsPerLoggingMaturation = "yyyy-MMM-dd @ hh:mm:ss.sssZ"

// Define console appender
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = "${encoderPattern}"
    }
}

if (fileLoggingFormat.toLowerCase() == "json") {
    appender("APP_LOG", RollingFileAppender) {
        file = loggingJsonFileName
        append = true
        encoder (LoggingEventCompositeJsonEncoder) {
            providers(LoggingEventJsonProviders) {
                timestamp(LoggingEventFormattedTimestampJsonProvider) {
                    fieldName = 'timestamp'
                    timeZone = 'UTC'
                    pattern = "${timeStampPatternAsPerLoggingMaturation}"
                }
                logLevel(LogLevelJsonProvider)
                loggerName(LoggerNameJsonProvider) {
                    fieldName = 'componentName'
                    shortenedLoggerNameLength = 35
                }
                message(MessageJsonProvider) {
                    fieldName = 'message'
                }
                uuid (UuidProvider) {
                    fieldName = 'messageId'
                }
                mdc (MdcJsonProvider)
            }
        }
        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${loggingFileDir}/${logAppName}-%d{yyyy-MM-dd}-%i.json"
            maxFileSize = FileSize.valueOf("10MB")   // Max size allowed for each log files
            maxHistory = 30 //Deletes older log files older than 30 days.
            totalSizeCap = FileSize.valueOf("100MB") // Total Max size allowed for the log files on disk
        }
    }
} else {
    //Define RollingFileAppender log
    appender("APP_LOG", RollingFileAppender) {
        file = loggingFileName
        encoder(PatternLayoutEncoder) {
            pattern = encoderPattern
        }
        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${loggingFileDir}/${logAppName}-%d{yyyy-MM-dd}-%i.log"
            maxFileSize = FileSize.valueOf("10MB")   // Max size allowed for each log files
            maxHistory = 30 //Deletes older log files older than 30 days.
            totalSizeCap = FileSize.valueOf("100MB") // Total Max size allowed for the log files on disk
        }
    }
}

if ( fileLoggingFormat.toLowerCase() == "json" ) {
    appender("eventLog", RollingFileAppender) {
        file = eventLoggingJsonFileName
        append = true
        encoder (LoggingEventCompositeJsonEncoder) {
            providers(LoggingEventJsonProviders) {
                timestamp(LoggingEventFormattedTimestampJsonProvider) {
                    fieldName = 'timestamp'
                    timeZone = 'UTC'
                    pattern = "${timeStampPatternAsPerLoggingMaturation}"
                }
                logLevel(LogLevelJsonProvider)
                loggerName(LoggerNameJsonProvider) {
                    fieldName = 'componentName'
                    shortenedLoggerNameLength = 35
                }
                message(MessageJsonProvider) {
                    fieldName = 'message'
                }
                uuid (UuidProvider) {
                    fieldName = 'messageId'
                }
                mdc (MdcJsonProvider)
            }
        }
        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${loggingFileDir}/brim_event-%d{yyyy-MM-dd}-%i.log"
            maxFileSize = FileSize.valueOf("10MB")   // Max size allowed for each log files
            maxHistory = 30 //Deletes older log files older than 30 days.
            totalSizeCap = FileSize.valueOf("100MB") // Total Max size allowed for the log files on disk
        }
    }
} else {
    // Define RollingFileAppender log
    appender("eventLog", RollingFileAppender) {
        file = eventLoggingFileName
        encoder(PatternLayoutEncoder) {
            pattern = encoderPattern
        }
        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${loggingFileDir}/brim_event-%d{yyyy-MM-dd}-%i.log"
            maxFileSize = FileSize.valueOf("10MB")   // Max size allowed for each log files
            maxHistory = 30 //Deletes older log files older than 30 days.
            totalSizeCap = FileSize.valueOf("100MB") // Total Max size allowed for the log files on disk
        }
    }
}

println "Application log file location [${Environment.current}]: ${loggingFileDir}"

// Set the root logger level.
if (Environment.current == Environment.PRODUCTION) {
    root(ERROR, ['STDOUT', 'APP_LOG'])
} else {
    root(ERROR, ['STDOUT', 'APP_LOG'])
}

logger ("eventLog", INFO, ['eventLog'], false)

logger ("banner.event.publisher.consumer.BepConsumerGrailsPlugin", INFO, ['STDOUT', 'APP_LOG'], false)

logger("net.hedtech.jms.container.manager.SpringDMLCManagerService", INFO, ['STDOUT', 'APP_LOG'], false)

logger ("banner_recruit_integration_app.BootStrap", INFO, ['STDOUT', 'APP_LOG'], false)

logger ("net.hedtech.r2b.loader.LoadService", WARN, ['STDOUT', 'APP_LOG'], false)

// uncomment to debug Recruiter-To-Banner message processing
//logger ("net.hedtech.banner.about.AboutBrimService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.general.BannerIdService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.general.EnterpriseIdService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.student.InvalidSurrogateIdService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.student.admissions.ApplicantService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.student.admissions.ApplicantSupplementalService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.student.admissions.ApplicantModifiedSupplementalService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.student.admissions.ApplicantApplicationIdService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.banner.applications.RecruitmentOpportunityService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.banner.applications.ApplicationStatusService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.util.MessageServiceHolder", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.dbmslock.DbmsLockService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.loader.LoaderServiceBase", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.loader.MatchService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.loader.PushService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.request.RequestServiceBase", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterApplicationIdService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterApplicationStatusService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterConnectionTestService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterIdService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterProspectService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterTestScoreService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.student.admissions.RecruiterChecklistService", DEBUG, ['APP_LOG'], false)
// logger ("net.hedtech.banner.student.recruiting.RecruitingItemsReceivedService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.banner.finaid.FinancialAidApplicationService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.banner.finaid.FinancialAidAwardService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.banner.finaid.FinancialAidNewProspectService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.banner.translations.EnterpriseIdTranslationService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterRejectStatusService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterOfferCodeService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterDecisionStatusService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.resource.RecruiterTransactionStatusService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.util.MessageServiceUtil", DEBUG, ['APP_LOG'], false)

//uncomment to trace BEP event internal processing
//logger ("net.hedtech.banner.command.CommandProcessorService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.database.SqlExecutorService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.event.ErrorLoggingService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.event.history.EventHistoryService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.parser.util.XmlXPathUtilityService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.processor.MessageProcessorService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.banner.threshold.ThresholdManagerService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.jms.registry.RegistryService", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.response.EventDispatcherService", DEBUG, ['APP_LOG'], false)

//uncomment to trace BEP event command processing
//logger ("net.hedtech.jms.consumer.handlers.CommandBase", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.jms.pubsub.BannerMessageListener", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.rabbit.pubsub.BannerRabbitMessageListener", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.jms.pubsub.BannerMessagePublisher", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.AdmitDateApplAcceptCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.AdmitDateInstAcceptCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.ApplicationStatusCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.ConfirmedDateDecisionCodeCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.ConfirmedDateDepositPaidCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.ConfirmedDateRegisteredCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.EnrolledDateApplAcceptCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.EnrolledDateDecisionCodeCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.EnrolledDateRegisteredCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.ErpIdAddedCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.BDMDocumentEventCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.UpdateSupplementItemsEventCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.ApplicationHistoryEventCommand", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.r2b.jms.event.command.GobuMapUpdateCommand", DEBUG, ['APP_LOG'], false)

// ******** Grails framework classes *********
//logger ("org.codehaus.groovy.grails.web.servlet", DEBUG)        // controllers
//logger ("org.codehaus.groovy.grails.web.pages", DEBUG)          // GSP
//logger ("org.codehaus.groovy.grails.web.sitemesh", DEBUG)       // layouts
//logger ("org.codehaus.groovy.grails.web.mapping.filter", DEBUG) // URL mapping
//logger ("org.codehaus.groovy.grails.web.mapping", DEBUG)        // URL mapping
//logger ("org.codehaus.groovy.grails.commons", DEBUG)           // core / classloading
//logger ("org.codehaus.groovy.grails.plugins", DEBUG)            // plugins
//logger ("org.codehaus.groovy.grails.orm.hibernate", DEBUG)      // hibernate integration
//logger ("org.springframework", DEBUG)                           // Spring IoC
//logger ("org.hibernate", DEBUG)                                 // hibernate ORM
//logger ("grails.converters", DEBUG)                             // JSON and XML marshalling/parsing
//logger ("grails.app.service.org.grails.plugin.resource", DEBUG) // Resource Plugin
//logger ("org.grails.plugin.resource", DEBUG)                    // Resource Plugin

// ******* uncomment to trace database connections and security authentication **********
//logger ("grails.plugin.springsecurity.cas.SpringSecurityCasGrailsPlugin", ALL, ['APP_LOG'], false)
//logger ("grails.plugin.springsecurity.web.GrailsSecurityFilterChain", ERROR, ['APP_LOG'], false)
//logger ("org.springframework.security.cas", ALL, ['APP_LOG'], false)
//logger ("org.jasig.cas", DEBUG, ['APP_LOG'], false)
//logger ("org.springframework.security",DEBUG,['APP_LOG'], false)
//logger ("grails.plugin.springsecurity.web", DEBUG, ['APP_LOG'], false)
//logger ("net.hedtech.jasig.cas.client", ALL, ['STDOUT','APP_LOG'], false)

//logger ("net.hedtech.banner.configuration.ApplicationConfigurationUtils",DEBUG)
//logger ("net.hedtech.banner.security.AccessControlFilters",DEBUG)
//logger ("org.apache.http.headers", DEBUG)
//logger ("org.apache.http.wire", DEBUG)
//logger ("net.hedtech.banner.security.BannerGrantedAuthorityService",DEBUG)
//logger ("org.jasig",DEBUG)

// uncomment to display RESTful API metrics
//logger ("net.hedtech.banner.restfulapi.RestfulApiServiceMetrics",DEBUG)

// uncomment to display hibernate sql and parameter bindings
//logger ("org.hibernate.SQL",DEBUG)
//logger ("org.hibernate.type.descriptor.sql.BasicBinder",DEBUG)

// uncomment to display groovy sql in the log (also uncomment grails.logging.jul.usebridge = true below)
//logger ("groovy.sql.Sql",DEBUG)
// Uncomment the package/artifact for specific logging.
// You may enable any of these using JMX (recommended) or within this file (which will require a restart).
// Note that settings for specific packages/artifacts will override those for the root logger.
// Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.


// ******** Grails framework classes *********
//logger("grails.app.controllers", DEBUG)                        // controllers
//logger("grails.app.services", DEBUG)                           // services
//logger("org.grails.web.mapping", DEBUG)                        // URL mapping
//logger("grails.plugins.DefaultGrailsPluginManager", DEBUG)     // grails plugins


// ******* Banner Security framework classes **********
//logger("net.hedtech.banner.security",DEBUG,['APP_LOG'])
//logger("net.hedtech.banner.db", DEBUG)
//logger("net.hedtech.banner.security.BannerAccessDecisionVoter", DEBUG)
//logger("net.hedtech.banner.security.BannerAuthenticationProvider", DEBUG)
//logger("net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", DEBUG)
//logger("net.hedtech.banner.security.BannerUser", DEBUG)


// ******* Grails and Spring Security framework classes **********
//logger("grails.plugin.springsecurity", DEBUG)
//logger("grails.plugin.springsecurity.web.GrailsSecurityFilterChain", DEBUG)
//logger("org.springframework.security", DEBUG)


//******** CAS & SAML classes **********
//logger ("org.jasig.cas.client.session.SingleSignOutFilter", DEBUG)
//logger ("org.jasig.cas.client.session.SingleSignOutHandler", DEBUG)
//logger ("org.jasig.cas", DEBUG)
//logger ("grails.plugin.springsecurity", DEBUG)
//logger ("net.hedtech.banner.security.BannerSamlSessionFilter", DEBUG)
//logger ("net.hedtech.banner.security.CasAuthenticationProvider", DEBUG)
//logger ("net.hedtech.banner.security.BannerSamlAuthenticationProvider", DEBUG)
//logger ("net.hedtech.jasig.cas.client", DEBUG)
//logger ("org.springframework.security.cas.web.CasAuthenticationFilter", DEBUG)
//logger ("org.springframework.security.web.FilterChainProxy", DEBUG)
//logger ("org.grails.plugin.springsecurity.saml", DEBUG)

// ******* hibernate ORM **********
//logger("org.hibernate.type", DEBUG)
//logger("org.hibernate.SQL", DEBUG)
// activate logging for groovy sql (also uncomment debug 'groovy.sql.Sql' in log4j configuration above)
//grails.logging.jul.usebridge = true

//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("brim-logback")

System.out.println("External App Log Configure Done !")