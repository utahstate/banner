/*********************************************************************************
Copyright 2012-2016 Ellucian Company L.P. and its affiliates.
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

    * Transaction timeout (defaults to 30 seconds if not specified)

    * Administrative Users DataSource Configuration

    * Self Service Users DataSource Configuration

    * Banner 8 SS URL

    * Image Path Configuration

***************************************************************************** **/



/** ****************************************************************************
 *                                                                             *
 *                         On-Line Help Configuration                          *
 *                                                                             *
 *******************************************************************************/
onLineHelp.url = (System.getenv('ONLINEHELP_URL') ?: "http://HOST:PORT/banner9OH" )


/** ****************************************************************************
 *                                                                              *
 *              Transaction timeout Configuration (in seconds)                  *
 *                                                                              *
 ***************************************************************************** **/
banner.transactionTimeout = (System.getenv('BANNER_TRANSACTIONTIMEOUT') ?: 30 )


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


/** *****************************************************************************
 *                                                                              *
 *                 Self Service User DataSource Configuration                   *
 *                                                                              *
 ***************************************************************************** **/

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
//banner.picturesPath=System.getProperty('base.dir') + '/test/images'
banner.picturesPath=(System.getenv('BANNER_PICTURESPATH') ?: '/test/images' )
banner8.SS.url = (System.getenv('BANNER8_SS_URL') ?: 'http://<host_name>:<port_number>/<banner8>/' )

/********************************************************************************************
NOTE - MEP Context URLs are now fully supported when going from XE App to an 8x app

If your institution employs the use of MEP, key configuration changes and URL contexts must be updated accordingly.
The key for the string must be in the format:
mep.banner8.SS.url

The next thing to configure is the correct map list for it, such as:
mep.banner8.SS.url = [
     GVU: 'http://<host_name>:<port_number>/<banner8>/SMPL_GVU',
     BANNER: 'http://<host_name>:<port_number>/<banner8>/SMPL_BANNER'
  ]
********************************************************************************************/
