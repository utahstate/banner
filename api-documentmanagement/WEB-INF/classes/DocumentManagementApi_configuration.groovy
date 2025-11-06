/** *****************************************************************************
 Copyright 2014-2020 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

/** ****************************************************************************
 *                                                                              *
 *         Document Management API Configuration                                *
 *                                                                              *
 ***************************************************************************** **/

/** ****************************************************************************

 This file contains configuration needed by the Banner Document Management API 9.2
 web application. Please refer to the installation guide for
 additional information regarding the configuration items contained within this file.

 This configuration file contains the following sections:

 * BDM/ApplicationXtender Configuration

 * JMX Bean Names

 * Logging Configuration (Note: Changes here require restart -- use JMX to avoid the need restart)

 NOTE: DataSource and JNDI configuration resides in the cross-module
 'banner_configuration.groovy' file.
 ***************************************************************************** **/

/** ****************************************************************************
 *                                                                              *
 *          BDM/ApplicationXtender(AX) Configuration         					*
 *                                                                              *
 ***************************************************************************** **/

bdm.enabled = true  // boolean value
bdmserver.file.location = (System.getenv('BDMSERVER_FILE_LOCATION') ?: '/tmp/upload')
bdmserver.AXWebServicesUrl = (System.getenv('BDMSERVER_AXWEBSERVICEURL') ?: 'http://axserver/appxtender/axservicesinterface.asmx')
bdmserver.AXWebAccessURL = (System.getenv('BDMSERVER_AXWEBACCESSURL') ?: 'http://axserver/appxtender/')
bdmserver.Username = (System.getenv('BDMSERVER_USERNAME') ?: 'BDMAPI_SUPERUSER')
bdmserver.DataSource = (System.getenv('BDMSERVER_DATASOURCE') ?: 'TEST')
bdmserver.BdmDataSource = (System.getenv('BDMSERVER_BDMDATASOURCE') ?: 'TEST')
bdmserver.restrictedFile_ext = [".exe", ".zip"] //file extension restriction default values
bdmserver.defaultFileSize = 5  ////default file size in MB to be restricted

/** ****************************************************************************
 *                                                                             *
 *                        BDM Logging Configuration                            *
 *                                                                             *
 **************************************************************************** **/
// Note that logging is configured separately for each environment ('development', 'test', and 'production').
// By default, all 'root' logging is 'off'.  Logging levels for root, or specific packages/artifacts, should be configured via JMX.
// Note that you may enable logging here, but it:
//   1) requires a restart, and
//   2) will report an error indicating 'Cannot add new method [getLog]'. (although the logging will in fact work)
//
// JMX should be used to modify logging levels (and enable logging for specific packages). Any JMX client, such as JConsole, may be used.
//
// The logging levels that may be configured are, in order: ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF
//

// Logging configuration has moved to logback.groovy file under <App>/grails-app/conf/logback.groovy
/** *****************************************************************************
 *                                                                              *
 *                      ConfigJob (Platform 9.23)                               *
 *     Support for configurations to reside in the database.                    *
 *                                                                              *
 ***************************************************************************** **/
configJob.delay = 60000
configJob.interval = 12000
configJob.actualCount = -1

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

//cors.enabled = false

// Regex pattern for allowed origins.  If the origin supplied by the browser matches,
// then the CORS plugin will echo back the received Origin in the Allowed origin field
// of the response.
// Ex: cors.allow.origin.regex = '.*\\.ellucian\\.com'

//cors.allow.origin.regex = '.*\\.<instutitution>\\.<com>'

//targetServer = "weblogic" // For weblogic deployment
targetServer="tomcat" // For Tomcat Deployment