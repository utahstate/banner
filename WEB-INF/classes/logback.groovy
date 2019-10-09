/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/


import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import grails.util.BuildSettings
import grails.util.Environment
import grails.util.Metadata
import net.hedtech.banner.configuration.ExternalConfigurationUtils
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

ExternalConfigurationUtils.setupExternalLogbackConfig()

// Set the application name for logging purposes
def loggingAppName = Metadata.current.getApplicationName()

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

def targetDir = '/usr/local/tomcat/logs'
appender("BANNER_EVENT_PUBLISHER_LOG", RollingFileAppender) {
    file = "${targetDir}/${loggingAppName}.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5p %c{2} - %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${targetDir}/${loggingAppName}-%d{yyyy-MM-dd}.log"
        maxHistory = 20
    }
}

appender("EVENT_SUMMARY_LOG", RollingFileAppender) {
    file = "${targetDir}/bep_event_summary.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5p %c{2} - %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${targetDir}/bep_event_summary-%d{yyyy-MM-dd}.log"
        maxHistory = 20
    }
}
// Set the root logger level.
if (Environment.current == Environment.PRODUCTION) {
    root(INFO, ['BANNER_EVENT_PUBLISHER_LOG'])
} else {
    root(DEBUG, ['STDOUT', 'BANNER_EVENT_PUBLISHER_LOG'])
}

logger("bep_event_summary", INFO, ['EVENT_SUMMARY_LOG'], false)

//BEP Logs
logger 'com.sungardhe.eventing.events.processor.BusinessEventProcessor', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'com.sungardhe.eventing.metadata.service.impl.BusinessEventMetaDataServiceImpl', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'com.sungardhe.eventing.events.processor.BusinessEventProcessor', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'com.sungardhe.eventing.messaging.impl.BusinessEventPublisherImpl', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ExportBusinessEventDataService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.importEvents.ImportBusinessEventOverviewService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ImportEventsJsonService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ImportEventsService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.bep.events.ExportEventsService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false
logger 'net.hedtech.banner.export.ExcelExportService', INFO, ['BANNER_EVENT_PUBLISHER_LOG'],false

// Hibernate Logs
logger 'org.hibernate.SQL', OFF, ['STDOUT'],false
logger 'org.hibernate.type.descriptor.sql.BasicBinder', OFF, ['STDOUT'], false

//XE Application specific
logger "net.hedtech.banner.security.BannerAccessDecisionVoter", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerAuthenticationProvider", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security.CasAuthenticationProvider", OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger "net.hedtech.banner.security.SelfServiceBannerAuthenticationProvider", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security.BannerPreAuthenticatedFilter", OFF, ['STDOUT'], false
logger "net.hedtech.banner.security", OFF, ['STDOUT',], false
logger "net.hedtech.banner.db", OFF, ['STDOUT'], false
logger "net.hedtech.banner.general.configuration.ConfigJob", OFF, ['STDOUT'], false
logger "net.hedtech.banner.service", OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', OFF, ['STDOUT'], false

//Grails Plugin Info
logger('grails.plugins.DefaultGrailsPluginManager', OFF, ['STDOUT'], false)

// Spring Security
logger 'grails.plugin.springsecurity.SpringSecurityCoreGrailsPlugin', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.filter.DebugFilter', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.SpringSecurityUtils', OFF, ['STDOUT'], false
logger 'org.grails.plugin.springsecurity.saml', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.cas.SpringSecurityCasGrailsPlugin', OFF, ['STDOUT'], false
logger 'grails.app.filters', OFF, ['STDOUT'], false
logger 'org.springframework.security.cas', OFF, ['STDOUT'], false
logger 'org.jasig.cas', OFF, ['STDOUT'], false
logger 'grails.plugin.springsecurity.web.GrailsSecurityFilterChain',OFF,['STDOUT'], false
logger 'grails.plugin.springsecurity.web',OFF,['STDOUT'], false
logger 'org.springframework.security',OFF,['STDOUT'], false
logger 'net.hedtech.jasig.cas.client',OFF,['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'org.springframework.security.cas.web.CasAuthenticationFilter',OFF,['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'org.jasig.cas.client.session', OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'org.springframework.boot.web.servlet', OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false
logger 'net.hedtech.banner.ui.ss', OFF, ['STDOUT','BANNER_EVENT_PUBLISHER_LOG'], false

//  Configure JMX access.
//  The names used to register Mbeans must be unique for all applications deployed into the JVM.
//  This configuration should be updated for each instance of each application to ensure uniqueness.
jmxConfigurator("${loggingAppName}-logback")