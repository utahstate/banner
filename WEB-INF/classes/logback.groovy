
import grails.util.BuildSettings
import grails.util.Metadata
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

def targetDir = "/usr/local/tomcat/logs"
String appname = Metadata.current.getApplicationName()
ExternalConfigurationUtils.setupExternalLogbackConfig()

appender("FULL_STACKTRACE", FileAppender) {
    //file = "target/stacktrace.log"
    file = "${targetDir}/${appname}.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{[dd-MMM-yyyy @ HH:mm:ss.SSS]} %-5p %c{2} - %m%n"
    }
}
logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)

root(ERROR, ['STDOUT','FULL_STACKTRACE'])

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


logger("net.hedtech.banner.aip.ActionItemAsynchronousTaskProcessingEngineImpl", OFF)
logger("net.hedtech.banner.aip.post.job.ActionItemJobTaskManagerService",OFF)
logger("net.hedtech.banner.aip.post.job.ActionItemJobService",OFF)
logger("net.hedtech.banner.aip.post.job.ActionItemPostMonitor",OFF)
logger("net.hedtech.banner.aip.post.job.ActionItemPostCompositeService",OFF)
logger("net.hedtech.banner.service.ServiceBase", OFF)
logger("net.hedtech.banner.aip.post.grouppost.ActionItemPost", OFF)

logger("banner.general.ssb.app.BootStrap",OFF)
logger("banner.aip.BannerAipGrailsPlugin",OFF)
logger("org.quartz",INFO)

