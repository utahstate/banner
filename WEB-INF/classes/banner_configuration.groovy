/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/

 /** ****************************************************************************
 *                                                                              *
 *                Banner 9 Environment-Specific Configuration                   *
 *                                                                              *
 ***************************************************************************** **/

/*
This file contains shared configuration needed by Banner 9 web applications (aka Banner 9 Modules)
deployed within an environment (e.g., a TEST or PROD).  That is, this is a shared
(aka global) configuration file that may be used by all Banner 9 solutions
deployed into the same environment (and thus deployed to point to the same database).

Please refer to the administration guide for detailed explanations of the configuration items.

This configuration file contains the following sections:
     * On-line Help Configuration

    * Transaction timeout (defaults to 30 seconds if not specified)

    * Administrative Users DataSource Configuration

***************************************************************************** **/
/** ****************************************************************************
  *                                                                             *
  *                         On-Line Help Configuration                          *
  *                                                                             *
  *******************************************************************************/
 //
 onLineHelp.url = (System.getenv('ONLINEHELP_URL') ?: "http://HOST:PORT/banner9OH" )

/** ****************************************************************************
 *              Transaction timeout Configuration (in seconds)                  *
 ***************************************************************************** **/

banner.transactionTimeout = (System.getenv('BANNER_TRANSACTIONTIMEOUT') ?: 30 )

/** ****************************************************************************
 *                         Banner 8 Self-Service URL prefix                    *
 *******************************************************************************/

banner8.SS.url = (System.getenv('BANNER8_SS_URL') ?: 'http://<host_name>:<port_number>/<banner8>/' )

/** ****************************************************************************
 *                                                                              *
 *              Administrative User DataSource Configuration                    *
 *                                                                              *
 ***************************************************************************** **/


bannerDataSource {

    // JNDI configuration for use in 'production' environment
    jndiName = "jdbc/bannerDataSource"

    // Local configuration for use in 'development' and 'test' environments
    url   = "jdbc:oracle:thin:@HOST:PORT:SID"

    username = "USERNAME"
    password = "PASSWORD"
    driver   = "oracle.jdbc.OracleDriver"

    // Local configuration for using elvyx to view SQL statements that are bound. To enable this driver, simply uncomment the
    // elvyx driver and url below. Do NOT comment out the 'myDataSource.url' above -- it is still needed for the authentication data source.
    // To use elvyx, download from "http://www.elvyx.com", unzip, and run from it's top-level directory: java -jar lib/elvyx-1.0.24.jar
    //
    //elvyx.driver = "com.elvyx.Driver"
    //elvyx.url    = "jdbc:elvyx://localhost:4448/?elvyx.real_driver=${bannerDataSource.driver}&elvyx.real_jdbc=${bannerDataSource.url}&user=${bannerDataSource.username}&password=${bannerDataSource.password}"
}

bannerSsbDataSource {

    // JNDI configuration for use in 'production' environment
    //
    jndiName = "jdbc/bannerSsbDataSource"

    // Local configuration for use in 'development' and 'test' environments
    //
    url   = "jdbc:oracle:thin:@HOST:PORT:SID"

    username = "USERNAME"
    password = "PASSWORD"
    driver   = "oracle.jdbc.OracleDriver"

    // Local configuration for using elvyx to view SQL statements that are bound. To enable this driver, simply uncomment the
    // elvyx driver and url below. Do NOT comment out the 'myDataSource.url' above -- it is still needed for the authentication data source.
    // To use elvyx, download from "http://www.elvyx.com", unzip, and run from it's top-level directory: java -jar lib/elvyx-1.0.24.jar
    //
    //elvyx.driver = "com.elvyx.Driver"
    //elvyx.url    = "jdbc:elvyx://localhost:4448/?elvyx.real_driver=${bannerSsbDataSource.driver}&elvyx.real_jdbc=${bannerSsbDataSource.url}&user=${bannerSsbDataSource.username}&password=${bannerSsbDataSource.password}"
}

/* Location for images */
banner.picturesPath= (System.getenv('BANNER_PICTURESPATH') ?: '/test/images')
banner.defaultPhoto = (System.getenv('BANNER_DEFAULTPHOTO') ?: '/test/images/default.jpg')

/** *****************************************************************************
 *                                                                              *
 *                 Eliminate access to the WEB-INF folder                       *
 *                                                                              *
 ***************************************************************************** **/
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**']
grails.resources.adhoc.excludes = ['/WEB-INF/**']

applicationNavigator=(Boolean.parseBoolean(System.getenv('APPLICATIONNAVIGATOR') ?: true))
