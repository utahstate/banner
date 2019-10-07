/******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
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

import java.nio.charset.Charset

// This method is used for setup the external LogbackConfig
ExternalConfigurationUtils.setupExternalLogbackConfig()

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern =
                '%clr(%d{[dd-MMM-yyyy @ HH:mm:ss.SSS]}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

// Set the logging output directory
def loggingFileDir
switch (Environment.current) {
    case Environment.PRODUCTION:
        loggingFileDir = System.properties["bdm.logging.dir"]
        break
    default: // Development or test mode
        loggingFileDir = BuildSettings.TARGET_DIR
        break
}
loggingFileDir = loggingFileDir ?: "/usr/local/tomcat/logs"
def logAppName = Metadata.current.getApplicationName()
def loggingFileName = "${loggingFileDir}/${logAppName}.log".toString()

println "Application log file location [${Environment.current}]: ${loggingFileDir}"

appender("appLog", FileAppender) {
    file = loggingFileName
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{[yyyy-MM-dd @ HH:mm:ss.SSS]} %-5p %c{2} - %m%n"
    }
}

switch (Environment.current.name.toString()) {
    case 'development':
        root(ERROR, ['STDOUT', 'appLog'])
        logger "net.hedtech.banner.configuration.ApplicationConfigurationUtils", INFO, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.representations", ERROR, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.supplemental.SupplementalDataService", ERROR, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.imaging", DEBUG, ['STDOUT', 'appLog'], false
        logger "net.hedtech.bdm", DEBUG, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.imaging.BDMManager", DEBUG, ['STDOUT', 'appLog'], false
        logger "net.hedtech.bdm.services", DEBUG, ['STDOUT', 'appLog'], false
        break
    case 'test':
        root(ERROR, ['STDOUT', 'appLog'])
        break
    case 'production':
        root(ERROR, ['STDOUT', 'appLog'])
        logger "net.hedtech.banner.configuration.ApplicationConfigurationUtils", INFO, ['STDOUT', 'appLog'], false
        logger "rails.app.service", ERROR, ['STDOUT', 'appLog'], false
        logger "grails.app.controller", INFO, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.representations", INFO, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.supplemental.SupplementalDataService", INFO, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.imaging", ERROR, ['STDOUT', 'appLog'], false
        logger "net.hedtech.bdm", ERROR, ['STDOUT', 'appLog'], false
        logger "net.hedtech.banner.imaging.BDMManager", DEBUG, ['STDOUT', 'appLog'], false
        logger "net.hedtech.bdm.services", DEBUG, ['STDOUT', 'appLog'], false
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