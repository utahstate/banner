/******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/**
 *    This is the default logging configuration for the Application Navigator application.
 *    To override this configuration in a 'production' environment, following the
 *    directions in the applicationNavigator_logback.example file.
 *
 *    In a PRODUCTION environment:
 *        This file will reside in the Banner application's classpath.
 *    Each Banner application will have its own logging file.  Example: applicationNavigator.log
 *    Banner logging files are set up to roll on a daily basis with SizeAndTimeBasedRolling Policy. Example: applicationNavigator-2019-03-05-0.log
 *
 *        Default settings for Banner production logging:
 *           Logging is directed to a file (not the console).
 *           Log file directory: <user.home>/banner_logs
 *        Default settings for Banner logging:
 *           Root logger level: ERROR
 *           Logging is turned off for specific packages (see below).
 *           Logging is turned DEBUG for specific packages (see below) with commented. This should be uncomment based on requirement.
 *
 *    In a DEVELOPMENT or TEST environment:
 *        This file resides in the project's /grails-app/conf directory.
 *        User can specify the different logging directory using the following system property:
 *            -Dbanner.logging.dir=<full directory path>  (THIS MUST BE AN ABSOLUTE PATH WITH WRITE PERMISSION)
 *            Example: -Dbanner.logging.dir=/home/tomcat/logs
 *
 *        Default settings for Banner development and test logging:
 *            Logging is directed to a file and the console.
 *            Log file directory: <project home>/build
 *            Root logger level: ERROR
 *            Logging is turned off for specific packages (see below).
 *        If different logging directory is not configured then the log files will be generated in the build folder as Provided default by Grails.
 *
 *    Banner logging files are set up to roll on a daily basis.  Example: EmployeeSelfService-2019-03-05.log
 *    Use JMX to change logger levels for ROOT or specific packages/artifacts.
 *    Valid logger levels in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
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
import net.hedtech.banner.configuration.ExternalConfigurationUtils
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
def loggingDir = System.properties["banner.logging.dir"] ?: "/usr/local/tomcat/logs"

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


//  Turn off package/artifact specific logging.
//  You may enable any of these using JMX (recommended) or within this file (which will require a restart).
//  Note that settings for specific packages/artifacts will override those for the root logger.
//  Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.
//******* Application packages *******
//logger("net.hedtech.seamless", OFF)
//logger("net.hedtech.banner.service", OFF)

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


//******* Application packages *******
//logger("net.hedtech.banner.ui.ss", DEBUG)


// ******* Configure JMX access *******
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")