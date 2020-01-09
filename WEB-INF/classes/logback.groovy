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

// Set the logging output directory
//def loggingDir = System.properties["banner.logging.dir"] ?: BuildSettings.TARGET_DIR
def loggingDir = '/usr/local/tomcat/logs'
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
    root(ERROR, ['APP_LOG'])
} else {
    root(ERROR, ['STDOUT', 'APP_LOG'])
}

// Uncomment the package/artifact for specific logging.
// You may enable any of these using JMX (recommended) or within this file (which will require a restart).
// Note that settings for specific packages/artifacts will override those for the root logger.
// Setting any of these to 'off' will prevent logging from that package/artifact regardless of the root logging level.
//logger("net.hedtech.banner.service", OFF)
//logger("net.hedtech.banner.representations", OFF)
//logger("BannerUiSsGrailsPlugin", OFF)

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
//logger("org.hibernate.cache.*", OFF)
//logger("net.sf.ehcache.*", OFF)
//logger("asset.pipeline.gradle", OFF)

//******* Application packages *******
//logger("net.hedtech.banner.ui.ss", DEBUG)


// ******* Configure JMX access *******
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")