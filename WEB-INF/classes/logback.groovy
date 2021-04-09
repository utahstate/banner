/******************************************************************************
 Copyright 2019 - 2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/**
 *    This is the default logging configuration for the application.
 *
 *    Each Banner application will have its own logging file.  Example: StudentRegistrationSsb.log
 *    Banner logging files are set up to roll on a daily basis with SizeAndTimeBasedRolling Policy. Example: StudentRegistrationSsb-2019-03-05-0.log
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

def loggingAppName =  Metadata.current.getApplicationName()   // The application name for logging purposes.


// Set the logging output directory
def loggingDir = System.properties["banner.logging.dir"] ?: '/usr/local/tomcat/logs'
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
    appender("APP_LOG", RollingFileAppender) {
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
                message(MessageJsonProvider) {
                    fieldName = 'message'
                }
                uuid (UuidProvider) {
                    fieldName = 'messageId'
                }
                mdc (MdcJsonProvider)
            }
        }
        rollingPolicy(FixedWindowRollingPolicy) {
            maxIndex = 30
            fileNamePattern = "${loggingDir}/${loggingAppName}.json.%i"
        }
        triggeringPolicy(SizeBasedTriggeringPolicy) {
            maxFileSize = "10MB"
        }
        logger("appLog", ERROR, ['APP_LOG'], true)
    }
} else {
    // Define RollingFileAppender log
    appender("APP_LOG", RollingFileAppender) {
        file = "${loggingDir}/${loggingAppName}.log"
        encoder(PatternLayoutEncoder) {
            pattern = encoderPattern
        }
        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${loggingDir}/${loggingAppName}-%d{yyyy-MM-dd}-%i.log"
            maxFileSize = FileSize.valueOf("10MB")   // Max size allowed for each log files
            maxHistory = 30 //Deletes older log files older than 30 days.
            totalSizeCap = FileSize.valueOf("100MB") // Total Max size allowed for the log files on disk
        }
        logger("appLog", ERROR, ['APP_LOG'], true)
    }
}
println "Application log file location [${Environment.current}]: ${loggingDir}"


// Set the root logger level.
if (Environment.current == Environment.PRODUCTION) {
    root(ERROR, ['STDOUT', 'APP_LOG'])
} else {
    root(ERROR, ['STDOUT', 'APP_LOG'])
}


//  Turn off package/artifact specific logging.
//  You may enable any of these using JMX (recommended) or within this file (which will require a restart).
//  Note that settings for specific packages/artifacts will override those for the root logger.
//  Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.
logger("net.hedtech.banner.service", OFF)
logger("net.hedtech.banner.representations", OFF)
logger("BannerUiSsGrailsPlugin", OFF)

// ******** Grails framework classes *********
logger("org.codehaus.groovy.grails.web.servlet", OFF)        // controllers
logger("org.codehaus.groovy.grails.web.pages", OFF)          // GSP
logger("org.codehaus.groovy.grails.web.sitemesh", OFF)       // layouts
logger("org.codehaus.groovy.grails.web.mapping.filter", OFF) // URL mapping
logger("org.codehaus.groovy.grails.web.mapping", OFF)        // URL mapping
logger("org.codehaus.groovy.grails.commons", OFF)            // core / classloading
logger("org.codehaus.groovy.grails.plugin", OFF)            // plugin
logger("org.codehaus.groovy.grails.orm.hibernate", OFF)      // hibernate integration
logger("org.springframework", OFF)                           // Spring IoC
logger("org.hibernate", OFF)                                 // hibernate ORM
logger("grails.converters", OFF)                             // JSON and XML marshalling/parsing
logger("grails.app.service.org.grails.plugin.resource", OFF) // Resource Plugin
logger("org.grails.plugin.resource", OFF)                    // Resource Plugin

// ******* Security framework classes **********

logger("net.hedtech.banner.security.*", OFF)
logger("net.hedtech.banner.db", OFF)
logger("net.hedtech.banner.security.BannerAccessDecisionVoter", OFF)
logger("net.hedtech.banner.security.BannerAuthenticationProvider", OFF)
logger("net.hedtech.banner.security.CasAuthenticationProvider", OFF)
logger("net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", OFF)
logger("net.hedtech.banner.security.BannerUser", OFF)
logger("grails.plugin.springsecurity", OFF)
logger("org.springframework.security", OFF)
logger("org.apache.http.headers", OFF)
logger("org.apache.http.wire", OFF)
logger("org.hibernate.type", OFF)
logger("org.hibernate.SQL", OFF)

logger("org.hibernate.*", OFF)                                 // hibernate ORM
logger("javax.servlet.http.HttpSessionListener", OFF)
logger("net.hedtech.banner.service.HttpSessionService", OFF)
logger("net.sf.*", OFF)
logger("de.javakaffee.*", OFF)
logger("org.hibernate.cache.*", OFF)
logger("net.sf.ehcache.*", OFF)

logger("asset.pipeline.gradle", OFF)

logger("grails.plugins.DefaultGrailsPluginManager", OFF)
logger("net.hedtech.banner.student.registration", OFF)

//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")