# Bandock Banner 9 Self Service Base

Banner 9 Self Service Base is a base image to build the Banner 9 self service application on.  Variables can be added from ENV, config file or docker secrets. 

## Tags

https://hub.docker.com/r/edurepo/banner9-selfservice/

- tomcat8-jre8-alpine
- tomcat8-oraclejava8-oraclelinux7

## How to use

This base image is designed to have Banner 9 applications built on it. It is built for those applciations that require both banproxy and ban_ss_user or commonly referred to as the self service apps.  Multiple builds are available.  At a minimum both builds of alpine and oracle linux will be available.  Alpine for its small size and oracle linux for Banner support compliance.

Properties can be loaded from multiple sources: environment variables, docker secrets, config file or combination of sources. They are appended to catalina.properties before tomcat starts. For tomcat to function the following properties need to be set: bannerdb.jdbc, banproxy.username, banproxy.username, banproxy.initialsize, banproxy.maxtotal, banproxy.maxidle, banproxy.maxwait, banssuser.username, banssuser.password, banssuser.initialsize, banssuser.maxtotal, banssuser.maxidle, banssuser.maxwait, cas.url, banner9.url. If they are not configued default values will be set from environment variables from the Dockerfile.

## Examples

There are example of banner 9 self service apps with environment variables, external config files, and internal config files using environment variables in the folder examples.

- StudentRegistrationSSB - external config
- StudentRegistrationSSB - config files using internal variables
- ApplicationNavigator - config files using env variables

### Properties from Environment Variables

Environment variables are used to setup the base parameters for tomcat to connect to the database. They are set as environment variables so that the base image can be used in different Banner instances.

### Defaults

If an environment variable is not specified at runtime then the defaults for that variable will be used.  At a minimum BANNERDB_JDBC, BANPROXY_PASSWORD,  BANSSUSER_PASSWORD, CAS_URL and BANNER9_URL need to be set. It is highly recommended setting the TIMEZONE to the timezone of your Banner database.

### Environment Variables

```Shell
TIMEZONE -  default: America/New_York
XMS - default: 2g
XMX - default: 4g
BANNERDB_JDBC - default: jdbc:oracle:thin:@//oracle.example.edu:1521/prod
BANPROXY_USERNAME - default: banproxy
BANPROXY_PASSWORD - default: password
BANPROXY_INITALSIZE - default: 25
BANPROXY_MAXTOTAL - default: 400
BANPROXY_MAXIDLE - default: -1
BANPROXY_MAXWAIT - default: 30000  
BANSSUSER_USERNAME - default: ban_ss_user
BANSSUSER_PASSWORD - default: password
BANSSUSER_INITALSIZE - default: 25
BANSSUSER_MAXTOTAL - default: 400
BANSSUSER_MAXIDLE - default: -1
BANSSUSER_MAXWAIT - default: 30000
CAS_URL - default: https://cas.local.com/cas
BANNER9_URL - default: https://banner9.school.edu
```

### Properties from Config file

When loading from a config file, defaults are ignored and all parameters will need to be set in the file.

```Shell
bannerdb.jdbc = jdbc:oracle:thin:@//oracle.example.edu:1521/prod
banproxy.username = banproxy
banproxy.username = password
banproxy.initialsize = 25
banproxy.maxtotal = 400
banproxy.maxidle = -1
banproxy.maxwait = 30000
banssuser.username = ban_ss_user
banssuser.password = password
banssuser.initialsize = 25
banssuser.maxtotal = 400
banssuser.maxidle = -1
banssuser.maxwait = 30000
cas.url = https://cas.local.com/cas
banner9.url = https://banner9.school.edu
```