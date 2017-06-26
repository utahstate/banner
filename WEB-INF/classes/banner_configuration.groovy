/** ****************************************************************************
         Copyright 2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

/** ****************************************************************************
 *                Banner XE Environment-Specific Configuration                 *
 *******************************************************************************/

/** ****************************************************************************
  This file contains shared configuration information needed by Banner XE
  applications deployed within an environment (e.g., a TEST or PROD). That is,
  this is a shared (aka global) configuration file that may be used by all
  Banner XE solutions deployed into the same environment, and thus deployed to
  point to the same database.

  Please refer to the installation guide for detailed explanations of the
  configuration items.

  This configuration file contains the following sections:

    * On-line Help Configuration

    * Transaction timeout (defaults to 30 seconds if not specified)

    * Banner 8 Self-Service URL prefix

    * Administrative Users DataSource Configuration

    * Self Service Users DataSource Configuration

 *******************************************************************************/

/** ****************************************************************************
 *                         On-Line Help Configuration                          *
 *******************************************************************************/

onLineHelp.url = (System.getenv('ONLINEHELP_URL') ?: "http://HOST:PORT/banner9OH")

/** ****************************************************************************
 *              Transaction timeout Configuration (in seconds)                  *
 ***************************************************************************** **/

banner.transactionTimeout = (System.getenv('BANNER_TRANSACTIONTIMEOUT') ?: 30 )

/** ****************************************************************************
 *                         Banner 8 Self-Service URL prefix                    *
 *******************************************************************************/

banner8.SS.url = (System.getenv('BANNER8_SS_URL') ?: 'http://HOST:PORT/DAD/' )

/** ****************************************************************************
 *                The Mail Server's recipient limit.                           *
 *******************************************************************************/

 email.batch.size = (System.getenv('EMAIL_BATCH_SIZE') ?: 600)

/** ****************************************************************************
 *              Administrative User DataSource Configuration                    *
 ***************************************************************************** **/

bannerDataSource {

// JNDI configuration for use in 'production' environment

    jndiName = "jdbc/bannerDataSource"

// Local configuration for use in 'development' and 'test' environments

    url   = "jdbc:oracle:thin:@HOST:PORT:SID"
    username = "USERNAME"
    password = "PASSWORD"
    driver   = "oracle.jdbc.OracleDriver"

/** *****************************************************************************
    Local configuration for using elvyx to view SQL statements that are bound.
    To enable this driver, simply uncomment the elvyx driver and url below.
    Do NOT comment out the 'myDataSource.url' above -- it is still needed for
    the authentication data source. To use elvyx, download from
    "http://www.elvyx.com", unzip, and run from it's top-level directory:
    java -jar lib/elvyx-1.0.24.jar
 ***************************************************************************** **/

//  elvyx.driver = "com.elvyx.Driver"
//  elvyx.url    = "jdbc:elvyx://localhost:4448/?elvyx.real_driver=${bannerDataSource.driver}&elvyx.real_jdbc=${bannerDataSource.url}&user=${bannerDataSource.username}&password=${bannerDataSource.password}"
}

/** *****************************************************************************
 *                 Self Service User DataSource Configuration                   *
 ***************************************************************************** **/

bannerSsbDataSource {

// JNDI configuration for use in 'production' environment

    jndiName = "jdbc/bannerSsbDataSource"

// Local configuration for use in 'development' and 'test' environments

    url   = "jdbc:oracle:thin:@HOST:PORT:SID"
    username = "USERNAME"
    password = "PASSWORD"
    driver   = "oracle.jdbc.OracleDriver"

/** *****************************************************************************
    Local configuration for using elvyx to view SQL statements that are bound.
    To enable this driver, simply uncomment the elvyx driver and url below.
    Do NOT comment out the 'myDataSource.url' above -- it is still needed for
    the authentication data source. To use elvyx, download from
    "http://www.elvyx.com", unzip, and run from it's top-level directory:
    java -jar lib/elvyx-1.0.24.jar
 ***************************************************************************** **/
//  elvyx.driver = "com.elvyx.Driver"
//  elvyx.url    = "jdbc:elvyx://localhost:4448/?elvyx.real_driver=${bannerSsbDataSource.driver}&elvyx.real_jdbc=${bannerSsbDataSource.url}&user=${bannerSsbDataSource.username}&password=${bannerSsbDataSource.password}"
}

banner {
    picturesPath = (System.getenv('BANNER_PICTURESPATH') ?: '/test/images')
    // defaultPhoto = <Fully qualified path to the photo to use if no user photo is available.>
}


/** ***************************************************************************
Banner 8 SSB needs to support the ability to define a single Banner 9 Self Service application URL
in the WebTailor menu appended with the correct MEP code. So when the user selects the menu item in
Banner 8 SS, the URL request with the MEP code launches the Banner 9 application page correctly.
The menu entry for a given Banner 9 SS application in WEBTAILOR should follow the convention as
shown in the example below.

Menu Item URL Entry for Banner 9 Faculty Attendance Tracking SS Application:
******************************************************************************* **/

//http://e009070.ellucian.com:8105/FacultyAttendanceTrackingSsb/ssb/facultyAttendanceTracking?mepCode={mepCode}

/** ***************************************************************************
Banner 9 Application needs to support the ability to call a MEP context sensitive URL that maps to
the appropriate SSB 8x URL. The URL configurations enable the end user SSO access from Banner 9 SS
to Banner 8 SSB applications along with preserving the MEP code for that user session.

Banner 9 SS application configuration should follow the convention as shown in the example below.
For example, if we have Banner 9 Faculty Attendance Tracking App deployed with two MEP institutions (GVU and BANNER) having the following URL syntax.
******************************************************************************* **/

//http://e009070.ellucian.com:8105/FacultyAttendanceTrackingSsb/ssb/facultyAttendanceTracking?mepCode=BANNER
//http://e009070.ellucian.com:8105/FacultyAttendanceTrackingSsb/ssb/facultyAttendanceTracking?mepCode=GVU

/** ***************************************************************************
The configuration that would go in the Banner 9 SS application configuration groovy file that maps the MEP code in Banner 9 to the target Banner 8x SSB URL is shown below.
******************************************************************************* **/

//mep.banner8.SS.url = [
//        GVU : 'http://e009070.ellucian.com:8888/ssomanager/c/SSB?pkg=http://e009070.ellucian.com:9020/SMPL_GVU/',
//        BANNER : 'http://e009070.ellucian.com:8888/ssomanager/c/SSB?pkg=http://e009070.ellucian.com:9020/SMPL_BANNER/'
//]
