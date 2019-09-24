import ch.qos.logback.core.util.FileSize
import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset
import net.hedtech.banner.configuration.ExternalConfigurationUtils

ExternalConfigurationUtils.setupExternalLogbackConfig()

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

def encoderPattern = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] [%t] %-5p %c %X - %m%n"

def loggingAppName =  Metadata.current.getApplicationName()   // The application name for logging purposes.


// Set the logging output directory
def loggingDir
switch (Environment.current) {
    case Environment.PRODUCTION:
        loggingDir = '/usr/local/tomcat/logs'
        break
    default: // Development or test mode
        loggingDir = BuildSettings.TARGET_DIR
        break
}
loggingDir = loggingDir ?: "${System.properties["user.home"]}"


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
println "${loggingAppName} logging folder location [${Environment.current}]: ${loggingDir}"


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
//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")