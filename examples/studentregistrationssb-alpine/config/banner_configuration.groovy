/*********************************************************************************
Copyright 2012-2017 Ellucian Company L.P. and its affiliates.
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
onLineHelp.url = "http://HOST:PORT/banner9OH"


/** ****************************************************************************
 *                                                                              *
 *              Transaction timeout Configuration (in seconds)                  *
 *                                                                              *
 ***************************************************************************** **/
banner.transactionTimeout = 300


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

/***************************************************************************************
 *                                                                                                                                                                                                *
 *                 Location of images                                                                                                                                *
 *                                                                                                                                                                                                 *
 ***************************************************************************************/
banner.picturesPath=System.getProperty('base.dir') + '/test/images'

/***************************************************************************************
 *                                                                                                                                                                                                *
 *                 Self Service Banner8 URL                                                                                                                                  *
 *                                                                                                                                                                                                 *
 ***************************************************************************************/
banner8.SS.locale.url =[
        default : 'http://<host_name>:<port_number>/DEFAULT/',
        en : 'http://<host_name>:<port_number>/EN/',
        en_US : 'http://<host_name>:<port_number>/enUS/',
        en_AU : 'http://<host_name>:<port_number>/enAU/',
        en_GB : 'http://<host_name>:<port_number>/enGB/',
        en_IE : 'http://<host_name>:<port_number>/enIE/',
        en_IN : 'http://<host_name>:<port_number>/enIN/',
        fr : 'http://<host_name>:<port_number>/FR/',
        fr_CA : 'http://<host_name>:<port_number>/frCA/',
        pt : 'http://<host_name>:<port_number>/PT/',
        es : 'http://<host_name>:<port_number>/ES/',
        ar : 'http://<host_name>:<port_number>/AR/'
]

/***************************************************************************************
 * NOTE - MEP Context URLs are now fully supported when going from Banner 9 Application  *
 * to an 8x application                                                                  *
 *                                                                                       *
 * If your institution employs the use of MEP,                                           *
 * key configuration changes and  URL contexts must be updated accordingly.              *
 *                                                                                       *
 *                                                                                       *
 mep.banner8.SS.locale.url=[
 GVU:
 [
 "default" : 'http://<host_name>:<port_number>/SMPL_GVU',
 "ar": 'sampleUrlAR ',
 "fr": 'sampleUrlfr',
 "fr_CA": 'sampleUrlfrca'
 ],
 BANNER :
 [
 "default" : 'http://<host_name>:<port_number>/SMPL_BANNER',
 "ar": 'sampleUrlAR',
 "fr": 'sampleUrlfr ',
 "fr_CA": 'sampleUrlfrca'
 ]
 ]
 ****************************************************************************************/
