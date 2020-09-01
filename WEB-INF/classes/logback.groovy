/******************************************************************************
 Copyright 2019-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/**
 *    This is the default logging configuration for the PlatformSandboxApp application.
 *
 *    Each Banner application will have its own logging file.  Example: PlatformSandboxApp.log
 *    Banner logging files are set up to roll on a daily basis with SizeAndTimeBasedRolling Policy. Example: PlatformSandboxApp-2019-03-05-0.log
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


def encoderPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5p %c{2} - %m%n"

def loggingAppName =  Metadata.current.getApplicationName()   // The application name for logging purposes.

// Set the logging output directory
def loggingDir = System.properties["banner.logging.dir"] ?: BuildSettings.TARGET_DIR
def fileLoggingFormat = "JSON"
def timeStampPatternAsPerLoggingMaturation = "yyyy-MMM-dd @ hh:mm:ss.sssZ"

// Define console appender
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = "${encoderPattern}"
    }
}

if ( fileLoggingFormat.toLowerCase() == "json" ) {
    appender("BANNER_EVENT_PUBLISHER_LOG", RollingFileAppender) {
        file = "${loggingDir}/${loggingAppName}.json"
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
                threadName(ThreadNameJsonProvider) {
                    fieldName = 'threadName'
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
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${loggingDir}/${loggingAppName}-%d{yyyy-MM-dd}.json"
            maxHistory = 20 //Deletes older log files older than 20 days.
        }
    }

    appender("EVENT_SUMMARY_LOG", RollingFileAppender) {
        file = "${loggingDir}/bep_event_summary.json"
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
                threadName(ThreadNameJsonProvider) {
                    fieldName = 'threadName'
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
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${loggingDir}/bep_event_summary-%d{yyyy-MM-dd}.json"
            maxHistory = 20 //Deletes older log files older than 20 days.
        }
    }
} else {
    // Define RollingFileAppender log
    appender("BANNER_EVENT_PUBLISHER_LOG", RollingFileAppender) {
        file = "${loggingDir}/${loggingAppName}.log"
        encoder(PatternLayoutEncoder) {
            pattern = encoderPattern

        }
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${loggingDir}/${loggingAppName}-%d{yyyy-MM-dd}.log"
            maxHistory = 20 //Deletes older log files older than 20 days.
        }
    }

    appender("EVENT_SUMMARY_LOG", RollingFileAppender) {
        file = "${loggingDir}/bep_event_summary.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = encoderPattern
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${loggingDir}/bep_event_summary-%d{yyyy-MM-dd}.log"
            maxHistory = 20 //Deletes older log files older than 20 days.
        }
    }
}
println "Application log file location [${Environment.current}]: ${loggingDir}"

// Set the root logger level.
if (Environment.current == Environment.PRODUCTION) {
    root(INFO, ['STDOUT', 'BANNER_EVENT_PUBLISHER_LOG'])
} else {
    root(INFO, ['STDOUT', 'BANNER_EVENT_PUBLISHER_LOG'])
}

logger("bep_event_summary", INFO, ['EVENT_SUMMARY_LOG'], false)

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
//logger("net.hedtech.banner.security", DEBUG)
//logger("net.hedtech.banner.db", DEBUG)
//logger("net.hedtech.banner.security.BannerAccessDecisionVoter", DEBUG)
//logger("net.hedtech.banner.security.BannerAuthenticationProvider", DEBUG)
//logger("net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", DEBUG)
//logger("net.hedtech.banner.security.BannerUser", DEBUG)

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

//******* Application packages *******
//logger("net.hedtech.banner.ui.ss", DEBUG)

//******** BEP Logs **********
logger 'com.sungardhe.eventing.events.processor.BusinessEventProcessor', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'com.sungardhe.eventing.metadata.service.impl.BusinessEventMetaDataServiceImpl', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'com.sungardhe.eventing.events.processor.BusinessEventProcessor', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'com.sungardhe.eventing.messaging.impl.BusinessEventPublisherImpl', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ExportBusinessEventDataService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.importEvents.ImportBusinessEventOverviewService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ImportEventsJsonService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ImportEventsService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ExportEventsService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.export.ExcelExportService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false

//******** Hibernate Logs **********
logger 'org.hibernate.SQL', OFF, ['STDOUT'],false
logger 'org.hibernate.type.descriptor.sql.BasicBinder', OFF, ['STDOUT'], false

//******** XE Application specific **********
logger "net.hedtech.banner.security.BannerAccessDecisionVoter", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerAuthenticationProvider", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security.CasAuthenticationProvider", OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger "net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerPreAuthenticatedFilter", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security", OFF, ['STDOUT',], false
logger 'net.hedtech.banner.security.BannerUser', OFF, ['STDOUT'], false
logger "net.hedtech.banner.db", OFF, ['STDOUT'], false
logger "net.hedtech.banner.general.configuration.ConfigJob", OFF, ['STDOUT'], false
logger "net.hedtech.banner.service", OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', OFF, ['STDOUT'], false

//******** Grails Plugin Info **********
logger('grails.plugins.DefaultGrailsPluginManager', OFF, ['STDOUT'], false)

//******** Spring Security **********
logger 'grails.plugin.springsecurity.SpringSecurityCoreGrailsPlugin', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.SpringSecurityUtils', OFF, ['STDOUT'], false
logger 'org.grails.plugin.springsecurity.saml', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.cas.SpringSecurityCasGrailsPlugin', OFF, ['STDOUT'], false
logger 'grails.app.filters', OFF, ['STDOUT'], false
logger 'org.springframework.security.cas', OFF, ['STDOUT'], false
logger 'org.jasig.cas', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.GrailsSecurityFilterChain',OFF,['STDOUT'], false
logger 'grails.plugin.springsecurity.web',OFF,['STDOUT'], false
logger 'org.springframework.security',OFF,['STDOUT'], false
logger 'net.hedtech.jasig.cas.client',OFF,['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'org.springframework.security.cas.web.CasAuthenticationFilter',OFF,['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'org.jasig.cas.client.session', OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'org.springframework.boot.web.servlet', OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'net.hedtech.banner.ui.ss', OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false

// ******* Configure JMX access *******
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")