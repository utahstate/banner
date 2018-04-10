# Application Navigator example using ENV varables

Building off the ENV variables that needed for the banner9-selfservice, the following are new ENV varabiles used in the configuration of appnav.  This example is for a CAS configuration.  

Each of the environment variables for application navigator have default values which are used if not set.  The list below includes the defaults.

## ENV Defaults

```Shell
SSBENABLED = false
SSBORACLEUSERSPROXIED = false
BANNER9_AFTERLOGOUTURL = http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator/logout/customLogout
BANNER9_HOMEPAGEURL = http://APPLICATION_NAVIGATOR_HOST:PORT/applicationNavigator
SEAMLESS_BRANDTITLE = Ellucian University
SEAMLESS_SESSIONTIMEOUT = 30
SEAMLESS_SESSIONTIMEOUTNOTIFICATION = 5
BANNER_ANALYSTICS_TRACKERID = 
BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER = true
ONLINEHELP_URL = http://HOST:PORT/banner9OH
BANNER_TRANSACTIONTIMEOUT = 30
BANNER_PICTURESPATH = /opt/banner/images
BANNER8_SS_URL = http://<host_name>:<port_number>/<banner8>/
```

## Build/Run

To build this example you will need to create a folder applicationNavigator in this directory, extract the applicationNavigator.war into that directory then copy the two .groovy config files into that directory.

### Build

```Shell
mkdir applicationNavigator
cd applicationNavigator
jar xvf ../applicationNavigator.war
cp ../WEB-INF/classes/* WEB-INF/classes/
cd ..
docker build -t banner/applicationnavigator:3.0 .
```

### Build and run with Docker Compose

```Shell
docker-compose up -d
```