 /******************************************************************************
 Copyright 2017-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
 
/** ****************************************************************************
 *                                                                              *
 *         ETranscriptAPI Configuration                                         *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

This file contains configuration needed by the ETranscriptAPI Banner 9
web application. Please refer to the administration guide for
additional information regarding the configuration items contained within this file.


     NOTE: DataSource and JNDI configuration resides in the cross-module
           'banner_configuration.groovy' file.

 // Logging configuration has moved to logback.groovy file under <App>/grails-app/conf/logback.groovy
***************************************************************************** **/
/** *****************************************************************************
 *                                                                              *
 *                         Self Service Support                                 *
 *                                                                              *
 ***************************************************************************** **/
/*ssbEnabled = true*/
ssbOracleUsersProxied = true
/** *****************************************************************************
 *                                                                              *
 *                      ConfigJob (Platform 9.23)                               *
 *     Support for configurations to reside in the database.                    *
 *     Platform 9.33.2.1 removes support for delay                              *
 *     Platform 9.34 changes interval to 24 hours.                              *
 *                                                                              *
 ***************************************************************************** **/
configJob {
    interval = 120000
    actualCount = -1
}



/* *****************************************************************************
 *                                                                             *
 *               Administrative Endpoint Support Enablement                    *
 *                                                                             *
 *******************************************************************************/
// Disabling 'administrativeBannerEnabled' (setting to 'false') will prevent the
// BannerAuthenticationProvider from attempting to authenticate users.
administrativeBannerEnabled = true  // default is 'true'


// Enable this if guest or proxy authentication is desired. This will cause the
// SelfServiceAuthenticationProvider to attempt authentication for 'non-PIDM' users.
//
guestAuthenticationEnabled = false // default is false

// Default web session timeout for self-service users, when not overriden by a web role
//
defaultWebSessionTimeout = 1800	// in seconds, so 1800 = 1/2 hour

/*******************************************************************************
 *                                                                             *
 *                                                                             *
 *                        Database BANPROXY settings                           *
 *                                                                             *
 *******************************************************************************/
// This setting sends all API database requests through the slower bannerDataSource.
// Disabling this setting does mean all audit user trails will be the username
// configured for your bannerSsbDataSource.

apiOracleUsersProxied=true

/*******************************************************************************
 *                                                                             *
 *                                                                             *
 *                        CORS (Cross Origin Resource Support)                 *
 *                                                                             *
 *******************************************************************************/
// These settings configure CORS support if required.
// By default browsers will not allow JavaScript or Java applets to
// retrieve resources (call these APIs) outside of the origin domain.
//
// The CORS plugin sets the header to disable the browser security check for
// domains matching the cors.allow.origin.regex pattern.  It's not recommended
// to open the pattern up as external sites could query your APIs and X-Site
// security would be compromised.

// Cors is disabled by default, set to true to enable.

cors.enabled = false

// Regex pattern for allowed origins.  If the origin supplied by the browser matches,
// then the CORS plugin will echo back the received Origin in the Allowed origin field
// of the response.
// Ex: cors.allow.origin.regex = '.*\\.ellucian\\.com'

cors.allow.origin.regex = '.*\\.<instutitution>\\.<com>'

/**************************************************************************************************
                                     Override "max" upper limit

LIST APIs support pagination with "max" and "offset" parameters.
API service apply two rules on page size paramerter "max".  First is "default" and second is "upper limit".
If LIST request comes without "max", it will be set to "default".
If LIST request comes with very high "max", it will be reset to "upper limit".
The values for "default" and "upper limit" varies from API to API and are decided based on performance of API.

In few scenarios, like Bulk load, clients want to fetch more records with each LIST request.
For that, it is necessary to override programmatic "max upper limit" with the configuration setting api.<resource name>.page.maxUpperLimit
Note: It might also be necessary to increase transaction timeout to fetch more records.
***************************************************************************************************/
//api.courses.page.maxUpperLimit=1000
//api.sections.page.maxUpperLimit=1000
//api."instructional-events".page.maxUpperLimit=1000

/*********************************************************************************
*                     Application Server Configuration                          *
* When deployed on Tomcat this configuration should be targetServer="tomcat"    *
* When deployed on Weblogic this configuration should be targetServer="weblogic"*
*********************************************************************************/
targetServer="tomcat"
