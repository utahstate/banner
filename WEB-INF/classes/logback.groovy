/******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/**
 *    This is the default logging configuration for the EmployeeSelfService application.
 *    To override this configuration in a 'production' environment, following the
 *    directions in the EmployeeSelfService_logback.example file.
 *
 *    In a PRODUCTION environment:
 *        This file will reside in the Banner application's classpath.
 *
 *        Default settings for Banner production logging:
 *           Logging is directed to a file (not the console).
 *           Log file directory: <user.home>/banner_logs
 *           Root logger level: ERROR
 *           Logging is turned off for specific packages (see below).
 *
 *    In a DEVELOPMENT or TEST environment:
 *        This file resides in the project's /grails-app/conf directory.
 *
 *        Default settings for Banner development and test logging:
 *            Logging is directed to a file and the console.
 *            Log file directory: <project home>/build
 *            Root logger level: ERROR
 *            Logging is turned off for specific packages (see below).
 *
 *    Banner logging files are set up to roll on a daily basis.  Example: EmployeeSelfService-2019-03-05.log
 *    Use JMX to change logger levels for ROOT or specific packages/artifacts.
 *    Valid logger levels in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
 */

import ch.qos.logback.core.util.FileSize
import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

ExternalConfigurationUtils.setupExternalLogbackConfig()

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

def encoderPattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%t] %-5p %c %X - %m%n"
def appName = Metadata.current.getApplicationName()

// Set the application name for logging purposes
def loggingAppName = appName

// Set the logging output directory
def loggingDir
switch (Environment.current) {
    case Environment.PRODUCTION:
        loggingDir = "/usr/local/tomcat/logs"
        break
    default: // Development or test mode
        loggingDir = BuildSettings.TARGET_DIR
        break
}

println "${appName} default logging folder location [${Environment.current}]: ${loggingDir}"

// Define console appender
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = "${encoderPattern}"
    }
}

// Define application log appender
appender("APP_LOG", RollingFileAppender) {
    file = "${loggingDir}/${loggingAppName}.log"
    encoder(PatternLayoutEncoder) {
        pattern = encoderPattern
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${loggingDir}/${loggingAppName}-%d.log"
        maxHistory = 10
        totalSizeCap = FileSize.valueOf("2GB")
    }
    logger("appLog", ERROR, ['APP_LOG'], true)
}

// Set the root logger level.
if (Environment.current == Environment.PRODUCTION) {
    root(ERROR, ['APP_LOG'])
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
logger("org.codehaus.groovy.grails.plugin", OFF)             // plugin
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

//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")