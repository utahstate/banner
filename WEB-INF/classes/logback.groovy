import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

jmxConfigurator()

appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = '%d{[dd-MMM-yyyy @ HH:mm:ss.SSS]} %-5p %c{2} - %m%n'
    }
}
//
//// See http://logback.qos.ch/manual/groovy.html for details on configuration
//appender('STDOUT', ConsoleAppender) {
//    encoder(PatternLayoutEncoder) {
//        charset = Charset.forName('UTF-8')
//
//        pattern =
//                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
//                        '%clr(%5p) ' + // Log level
//                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
//                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
//                        '%m%n%wex' // Message
//    }
//}

String appname = Metadata.current.getApplicationName()
def targetDir = '/usr/local/tomcat/logs'
ExternalConfigurationUtils.setupExternalLogbackConfig()
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/${appname}.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%d{[dd-MMM-yyyy @ HH:mm:ss.SSS]} %-5p %c{2} - %m%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}

root(ERROR, ['STDOUT'])

// Hibernate Logs
logger 'org.hibernate.SQL', ERROR, ['STDOUT'],false
logger 'org.hibernate.type.descriptor.sql.BasicBinder', ERROR, ['STDOUT'], false

//XE Application specific
logger "net.hedtech.banner.security.BannerAuthenticationProvider", DEBUG, ['STDOUT'], false
logger "net.hedtech.banner.security.CasAuthenticationProvider", DEBUG, ['STDOUT'], false
logger "net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", DEBUG, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerPreAuthenticatedFilter", ERROR, ['STDOUT'], false
logger "net.hedtech.banner.security", ERROR, ['STDOUT',], false
logger "net.hedtech.banner.db", ERROR, ['STDOUT'], false
logger "net.hedtech.banner.general.configuration.ConfigJob", ERROR, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', ERROR, ['STDOUT'], false


// Spring Security
logger 'grails.plugin.springsecurity.SpringSecurityCoreGrailsPlugin', ERROR, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', ERROR, ['STDOUT'], false
logger 'grails.plugin.springsecurity.SpringSecurityUtils', ERROR, ['STDOUT'], false
logger 'org.grails.plugin.springsecurity.saml', ERROR, ['STDOUT'], false
logger 'grails.plugin.springsecurity.cas.SpringSecurityCasGrailsPlugin', ERROR, ['STDOUT'], false
logger 'grails.app.filters', ERROR, ['STDOUT'], false
logger 'org.springframework.security.cas', ERROR, ['STDOUT'], false
logger 'org.jasig.cas', DEBUG, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.GrailsSecurityFilterChain',ERROR,['STDOUT'], false
logger 'grails.plugin.springsecurity.web',ERROR,['STDOUT'], false
logger 'org.springframework.security',ERROR,['STDOUT'], false
logger 'net.hedtech.jasig.cas.client',ALL,['STDOUT'], false
logger 'org.springframework.security.cas.web.CasAuthenticationFilter',ERROR,['STDOUT'], false
logger 'org.jasig.cas.client.session', DEBUG, ['STDOUT'], false
logger 'org.springframework.boot.web.servlet', DEBUG, ['STDOUT'], false
logger 'net.hedtech.banner.ui.ss', DEBUG, ['STDOUT'], false
logger 'net.hedtech.banner.general.audit', ALL, ['STDOUT'], false


//Quartz
logger("org.quartz",ERROR)
logger("quartz",ERROR)
logger( 'net.hedtech.banner.general.communication', ERROR)
logger( 'net.hedtech.banner.general.scheduler', ERROR)
logger( 'net.hedtech.banner.general.asynchronous', ERROR)