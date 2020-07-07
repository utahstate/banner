/******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/**
 *    This is the default logging configuration for the BannerGeneralSsb application.
 *
 *    Each Banner application will have its own logging file.  Example: BannerGeneralSsb.log
 *    Banner logging files are set up to roll on a daily basis with SizeAndTimeBasedRolling Policy. Example: BannerGeneralSsb-2019-03-05-0.log
 *
 *        Default settings for Banner logging:
 *           Root logger level: ERROR
 *           Logging is turned DEBUG for specific packages (see below) with commented. This should be uncomment based on requirement.
 *
 *        User can specify the different logging directory using the following system property:
 *            -Dbanner.logging.dir=<full directory path>  (THIS MUST BE AN ABSOLUTE PATH WITH WRITE PERMISSION)
 *            Example: -Dbanner.logging.dir=/home/tomcat/logs
 *
 *        If a different logging directory is not configured, then the log files will be generated in the build folder per Grails default.
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
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset
import net.hedtech.banner.configuration.ExternalConfigurationUtils

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// This line is only required in logback.groovy which is present in /grails-app/conf.
// This should be commented in the external logback.groovy
ExternalConfigurationUtils.setupExternalLogbackConfig()


def encoderPattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%t] %-5p %c %X - %m%n"

def loggingAppName =  Metadata.current.getApplicationName()   // The application name for logging purposes.

def targetDir = "/usr/local/tomcat/logs"
// Set the logging output directory
def loggingDir = System.properties["banner.logging.dir"] ?: ${targetDir}

// Define console appender
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = "${encoderPattern}"
    }
}

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
println "Application log file location [${Environment.current}]: ${loggingDir}"


// Set the root logger level.
if (Environment.current == Environment.PRODUCTION) {
    root(ERROR, ['STDOUT', 'APP_LOG'])
} else {
    root(ERROR, ['STDOUT', 'APP_LOG'])
}

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


// ******* Grails and Spring Security framework classes **********
//logger("grails.plugin.springsecurity", DEBUG)
//logger("grails.plugin.springsecurity.web.GrailsSecurityFilterChain", DEBUG)
//logger("org.springframework.security", DEBUG)


// ******** CAS & SAML classes **********
//logger("org.jasig.cas.client.session.SingleSignOutFilter", DEBUG)
//logger("org.jasig.cas.client.session.SingleSignOutHandler", DEBUG)
//logger("org.jasig.cas", DEBUG)
//logger("grails.plugin.springsecurity", DEBUG)
//logger("net.hedtech.banner.security.BannerSamlSessionFilter", DEBUG)
//logger("net.hedtech.banner.security.CasAuthenticationProvider", DEBUG)
//logger("net.hedtech.banner.security.BannerSamlAuthenticationProvider", DEBUG)
//logger("net.hedtech.jasig.cas.client", DEBUG)
//logger("org.springframework.security.cas.web.CasAuthenticationFilter", DEBUG)
//logger("org.springframework.security.web.FilterChainProxy", DEBUG)
//logger("org.grails.plugin.springsecurity.saml", DEBUG)


// ******* Hibernate ORM **********
//logger("org.hibernate.type", DEBUG)
//logger("org.hibernate.SQL", DEBUG)


// ******* Application packages and classes *******
//logger("banner.general.ssb.app.BootStrap", DEBUG)
//logger("net.hedtech.banner.service.ServiceBase", DEBUG)
//logger("net.hedtech.banner.ui.ss", DEBUG)
//logger("net.sf.*", DEBUG)


// ******* Action Item Processing (AIP) classes *******
//logger("banner.aip.BannerAipGrailsPlugin", DEBUG)
//logger("net.hedtech.banner.aip.ActionItemAsynchronousTaskProcessingEngineImpl", DEBUG)
//logger("net.hedtech.banner.aip.post.job.ActionItemJobTaskManagerService", DEBUG)
//logger("net.hedtech.banner.aip.post.job.ActionItemJobService", DEBUG)
//logger("net.hedtech.banner.aip.post.job.ActionItemPostMonitor", DEBUG)
//logger("net.hedtech.banner.aip.post.job.ActionItemPostCompositeService", DEBUG)
//logger("net.hedtech.banner.aip.post.grouppost.ActionItemPost", DEBUG)
//logger("org.quartz",DEBUG)


// ******* Configure JMX access *******
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")