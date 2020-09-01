/******************************************************************************
 Copyright 2019-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/**
 *    BDM application logging is configured separately for each environment (DEVELOPMENT, TEST, and PRODUCTION).
 *
 *     Logging file.     Example: DocumentManagementApi.log
 *
 *
 *    In a PRODUCTION environment:
 *        This file will reside in the application's classpath.
 *
 *        Default settings for logging:
 *           Logging is directed to a file (not the console).
 *           Log file directory: <user.home>/banner_logs
 *           Root logger level: ERROR
 *           Logging is turned off for specific packages (see below).
 *
 *        Optionally, you can specify the logging directory using the following system property:
 *            -Dbdm.logging.dir=<full directory path>  (THIS MUST BE AN ABSOLUTE PATH)
 *
 *            Example: -Dbdm.logging.dir=/home/tomcat/target/logs
 *
 *        Use JMX to change logger levels for ROOT or specific packages/artifacts.
 *
 *
 *    In a DEVELOPMENT or TEST environment:
 *        This file resides in the project's /grails-app/conf directory.
 *
 *        Default settings for BDM logging:
 *            Logging is directed to a file and the console.
 *            Log file directory: <project home>/build
 *            Root logger level: ERROR
 *            Logging is turned off for specific packages (see below).
 *
 *        Optionally, you can modify this file to change the logger level for ROOT or specific packages/artifacts.
 *
 *
 *    Valid logger levels in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
 */

import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter
import ch.qos.logback.core.util.FileSize
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider
import net.logstash.logback.composite.loggingevent.*
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// This method is used for setup the external LogbackConfig
// This line is only required in logback.groovy which is present in /grails-app/conf.
// This should be commented in the external logback.groovy
ExternalConfigurationUtils.setupExternalLogbackConfig()

def encoderPattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%t] %-5p %c %X - %m%n"

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern ="${encoderPattern}"
    }
}

// Set the logging output directory
def loggingFileDir
switch (Environment.current) {
    case Environment.PRODUCTION:
        loggingFileDir = System.properties["banner.logging.dir"]
        break
    default: // Development or test mode
        loggingFileDir = BuildSettings.TARGET_DIR
        break
}
loggingFileDir = loggingFileDir ?: "${System.properties["user.home"]}/bdm_logs"
def logAppName = Metadata.current.getApplicationName()
def loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()
def fileLoggingFormat = "JSON"
def timeStampPatternAsPerLoggingMaturation = "yyyy-MMM-dd @ hh:mm:ss.sssZ"

println "Application log file location [${Environment.current}]: ${loggingFileName}";

if (fileLoggingFormat.toLowerCase() == "json") {
    appender("APP_LOG", RollingFileAppender) {
        file = "${loggingFileDir}/${logAppName}.json"
        append = true
        encoder(LoggingEventCompositeJsonEncoder) {
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
                uuid(UuidProvider) {
                    fieldName = 'messageId'
                }
                mdc(MdcJsonProvider)
            }
        }
        rollingPolicy(FixedWindowRollingPolicy) {
            maxIndex = 30
            fileNamePattern = "${loggingFileDir}/${logAppName}.json.%i"
        }
        triggeringPolicy(SizeBasedTriggeringPolicy) {
            maxFileSize = "10MB"
        }
        logger("appLog", ERROR, ['APP_LOG'], true)
    }
} else {
    // Define RollingFileAppender log
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
        logger("appLog", ERROR, ['APP_LOG'], true)
    }
}

switch (Environment.current.name.toString()) {
    case 'development':
        root(ERROR, ['STDOUT', 'APP_LOG'])
        logger "net.hedtech.banner.configuration.ApplicationConfigurationUtils", INFO, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.representations", ERROR, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.supplemental.SupplementalDataService", ERROR, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.imaging", DEBUG, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.bdm", DEBUG, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.imaging.BDMManager", DEBUG, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.bdm.services", DEBUG, ['STDOUT', 'APP_LOG'], false
        break
    case 'test':
        root(ERROR, ['STDOUT', 'APP_LOG'])
        break
    case 'production':
        root(ERROR, ['STDOUT', 'APP_LOG'])
        logger "net.hedtech.banner.configuration.ApplicationConfigurationUtils", INFO, ['STDOUT', 'APP_LOG'], false
        logger "rails.app.service", ERROR, ['STDOUT', 'APP_LOG'], false
        logger "grails.app.controller", INFO, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.representations", INFO, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.supplemental.SupplementalDataService", INFO, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.imaging", ERROR, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.bdm", ERROR, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.banner.imaging.BDMManager", DEBUG, ['STDOUT', 'APP_LOG'], false
        logger "net.hedtech.bdm.services", DEBUG, ['STDOUT', 'APP_LOG'], false
        break
}

// ******** non-Grails classes (e.g., in src/ or grails-app/utils/) *********
logger "grails.app.controllers.net.hedtech.restfulapi.RestfulApiController", OFF
logger "grails.app.services", OFF
logger "net.hedtech.banner.general.overall.ldm", OFF
logger "grails.app.services", OFF
logger "org.apache.catalina", OFF
logger "org.apache.catalina.core", OFF
logger "org.apache.catalina.connector", OFF
logger "org.apache.catalina.util", OFF
logger "org.apache.catalina.startup", OFF
logger "org.apache.catalina.session", OFF
logger "org.apache.tomcat.util.digester", OFF
logger "org.apache.tomcat", OFF
logger "org.apache.tomcat.util.modeler", OFF
logger "org.apache.naming", OFF
logger "CacheHeadersGrailsPlugin", OFF
logger "org.codehaus.groovy.grails.web.context", OFF
logger "grails.app.resourceMappers.org.grails.plugin.resource", OFF
logger "org.codehaus.groovy.grails.web.converters.configuration", OFF
logger "net.sf.ehcache.hibernate", OFF
logger "org.codehaus.groovy.grails.context.support", OFF
logger "net.hedtech.banner.mep", OFF
logger "org.codehaus.groovy.grails.aop.framework.autoproxy", OFF
logger "org.codehaus.groovy.grails.web.filters", OFF
logger "org.apache.log4j.jmx", OFF
logger "grails.util", OFF
logger "grails.spring", OFF
logger "BannerCoreGrailsPlugin", OFF
logger "net.sf.ehcache.hibernate", OFF
logger "net.sf.ehcache.config", OFF
logger "net.sf.ehcache", OFF
logger "net.sf.ehcache.util", OFF
logger "org.codehaus.groovy.grails.validation", OFF
logger "org.apache.naming", OFF
logger "BootStrap", OFF
logger "grails.app.resourceMappers.org.grails.plugin.resource", OFF
logger "org.apache.coyote.http11", OFF

logger "net.hedtech.banner.service", OFF
logger "net.hedtech.banner.student", OFF
logger "net.hedtech.banner.student.catalog", OFF
logger "net.hedtech.banner.student.common", OFF
logger "net.hedtech.banner.student.registration", OFF
logger "net.hedtech.banner.student.schedule", OFF
logger "net.hedtech.banner.student.faculty", OFF
logger "net.hedtech.banner.student.generalstudent", OFF
logger "net.hedtech.banner.student.system", OFF
logger "net.hedtech.banner.representations", OFF
logger "BannerUiSsGrailsPlugin", OFF

// ******** Grails framework classes *********
logger "org.codehaus.groovy.grails.web.servlet", OFF        // controllers
logger "org.codehaus.groovy.grails.web.pages", OFF          // GSP
logger "org.codehaus.groovy.grails.web.sitemesh", OFF       // layouts
logger "org.codehaus.groovy.grails.web.mapping.filter", OFF // URL mapping
logger "org.codehaus.groovy.grails.web.mapping", OFF        // URL mapping
logger "org.codehaus.groovy.grails.commons", OFF            // core / classloading
logger "org.codehaus.groovy.grails.plugins", OFF            // plugins
logger "org.codehaus.groovy.grails.orm.hibernate", OFF      // hibernate integration
logger "org.springframework", OFF                           // Spring IoC
logger "org.hibernate", OFF                                 // hibernate ORM
logger "grails.converters", OFF                             // JSON and XML marshalling/parsing
logger "grails.app.service.org.grails.plugin.resource", OFF // Resource Plugin
logger "org.grails.plugin.resource", OFF                    // Resource Plugin

// ******* Security framework classes **********
logger "net.hedtech.banner.security", OFF
logger "net.hedtech.banner.db", OFF
logger "net.hedtech.banner.security.BannerAccessDecisionVoter", OFF
logger "net.hedtech.banner.security.BannerAuthenticationProvider", OFF
logger "net.hedtech.banner.security.CasAuthenticationProvider", OFF
logger "net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", OFF
logger "grails.plugin.springsecurity", OFF
logger "org.springframework.security", OFF
logger "org.apache.http.headers", OFF
logger "org.apache.http.wire", OFF

//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("DocumentManagementApi-logback")