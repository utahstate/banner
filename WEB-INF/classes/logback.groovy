/******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

import grails.util.BuildSettings
import grails.util.Metadata
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter


appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = '%d{[dd-MMM-yyyy @ HH:mm:ss.SSS]} %-5p %c{2} - %m%n'
    }
}

//def targetDir = BuildSettings.TARGET_DIR
def targetDir = '/usr/local/tomcat/logs'
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
//}
root(ERROR, ['STDOUT','FULL_STACKTRACE'])

// Hibernate Logs
logger 'org.hibernate.SQL', ERROR, ['STDOUT'],false
logger 'org.hibernate.type.descriptor.sql.BasicBinder', ERROR, ['STDOUT'], false

//XE Application specific
logger "net.hedtech.banner.security.BannerAccessDecisionVoter", ERROR, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerAuthenticationProvider", DEBUG, ['STDOUT'], false
logger "net.hedtech.banner.security.CasAuthenticationProvider", DEBUG, ['STDOUT','FULL_STACKTRACE'], false
logger "net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", DEBUG, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerPreAuthenticatedFilter", ERROR, ['STDOUT'], false
logger "net.hedtech.banner.security", ERROR, ['STDOUT',], false
logger "net.hedtech.banner.db", ERROR, ['STDOUT'], false
logger "net.hedtech.banner.general.configuration.ConfigJob", ERROR, ['STDOUT'], false
logger "net.hedtech.banner.service", ERROR, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', ERROR, ['STDOUT'], false

//Grails Plugin Info
logger('grails.plugins.DefaultGrailsPluginManager', OFF, ['STDOUT'], false)

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
logger 'net.hedtech.jasig.cas.client',ALL,['STDOUT','FULL_STACKTRACE'], false
logger 'org.springframework.security.cas.web.CasAuthenticationFilter',ERROR,['STDOUT','FULL_STACKTRACE'], false
logger 'org.jasig.cas.client.session', DEBUG, ['STDOUT','FULL_STACKTRACE'], false
logger 'org.springframework.boot.web.servlet', DEBUG, ['STDOUT','FULL_STACKTRACE'], false
logger 'net.hedtech.banner.ui.ss', DEBUG, ['STDOUT','FULL_STACKTRACE'], false
logger 'net.hedtech.banner.general.audit', ALL, ['STDOUT','FULL_STACKTRACE'], false

//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${appname}-logback")
