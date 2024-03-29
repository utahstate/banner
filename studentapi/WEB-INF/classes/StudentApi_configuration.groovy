/** *****************************************************************************
 Copyright 2013-2022 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

/** ****************************************************************************
 *                                                                              *
 *         Student API Configuration                                            *
 *                                                                              *
 ***************************************************************************** **/


ssbEnabled = true

useRestApiAuthenticationEntryPoint = true

/** *****************************************************************************
 *                                                                              *
 *               Supplemental Data Support Enablement                           *
 *                                                                              *
 ***************************************************************************** **/

targetServer="tomcat"


/*******************************************************************************
 *                                                                             *
 *                                                                             *
 *                        Database BANPROXY settings                           *
 *                                                                             *
 *******************************************************************************/
// The apiOracleUsersProxied setting should always be set to true for API application
apiOracleUsersProxied=true

//Added the below config to avoid platform job to be run for refreshing GUROCFG and formControllerMap
quartz {
    autoStartup = false
}

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

/*******************************************************************************
 *                                                                             *
 *                               X-Media-Type                                  *
 *                                                                             *
 *******************************************************************************/
// Set this to true to get the Accept request header as the X-Media-Type response header.
// This is provided to help callers to delay transitioning to full semantic versioning of the X-Media-Type response header.
restfulApi.useAcceptHeaderAsMediaTypeHeader = false
