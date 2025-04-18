/*********************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
 
 /** ****************************************************************************
 *                                                                              *
 *                Banner XE Environment-Specific Configuration                   *
 *                                                                              *
 ***************************************************************************** **/

/*
This file contains shared configuration needed by Banner XE web applications (aka Banner XE Modules)
deployed within an environment (e.g., a TEST or PROD).  That is, this is a shared 
(aka global) configuration file that may be used by all Banner XE solutions
deployed into the same environment (and thus deployed to point to the same database).

Please refer to the administration guide for detailed explanations of the configuration items. 

This configuration file contains the following sections:

    * On-line Help Configuration

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
banner.transactionTimeout = 30


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
banner.picturesPath=System.getProperty('base.dir') + '/test/images'

banner8.SS.url = 'http://<SSO MANAGER HOST>:<PORT>/ssomanager/c/SSB?pkg='

// For supporting Single Sign-On for a Multi-Entity Processing (MEP) Database, a property can be used that
// contains a list of the institutional MEP codes available in the database with their respective URL values.
// The URL value for each MEP code is the combination of the fully qualified SSO Manager SSB URL along
// with the Banner 8.x SSB Direct Access URL for the given MEP institution. Example below shows the
// configuration for two MEP institutions - NORTH and SOUTH.
// When using the mep.banner8.SS.url property, comment out the property banner8.SS.url
mep.banner8.SS.url = [
    NORTH : 'http://<SSO MANAGER HOST>:<PORT>/ssomanager/c/SSB?pkg=http://<BANNER8 SSB HOST>:<PORT>/SMPL_NORTH/',
    SOUTH : 'http://<SSO MANAGER HOST>:<PORT>/ssomanager/c/SSB?pkg=http://<BANNER8 SSB HOST>:<PORT>/SMPL_SOUTH/'
]



