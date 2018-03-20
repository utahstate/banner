# Bandock Banner 9 Self Service Base

## Tags
- tomcat8-jre8-alpine
- tomcat8-oraclejava8-oraclelinux7

## How to use
This base image is designed to have Banner 9 applications built on it. It is built for those applciations that require both banproxy and ban_ss_user.  Multiple builds are available.  At a minimum both builds of alpine and oracle linux will be available.  Alpine for its small size and oracle linux for Banner support compliance.  

Properties can be loaded from multiple sources: environment variables, docker secrets, config file or combination of sources. They are appended to catalina.properties before tomcat starts. For tomcat to function the following properties need to be set: bannerdb.jdbc, banproxy.username, banproxy.username, banproxy.initialsize, banproxy.maxtotal, banproxy.maxidle, banproxy.maxwait, banssuser.username, banssuser.password, banssuser.initialsize, banssuser.maxtotal, banssuser.maxidle, banssuser.maxwait, cas.servername, banner9.servername. If they are not configued default values will be set from environment variables from the Dockerfile.  


## Env Variables
Environment variables are used to setup the base parameters for tomcat to connect to the database. They are set as environment variables so that the base image can be used in different Banner instances.  

### Defaults Env Variables
If an environment variable is not specified at runtime then the defaults for that variable will be used.  At a minimum BANNERDB_JDBC, BANPROXY_PASSWORD,  BANSSUSER_PASSWORD, CAS_URL and BANNER9_URL need to be set.  

- TIMEZONE -  default: America/New_York
- XMS - default: 2g
- XMX - default: 4g
- BANNERDB_JDBC - default: jdbc:oracle:thin:@//oracle.example.edu:1521/prod
- BANPROXY_USERNAME - default: banproxy
- BANPROXY_PASSWORD - default: password
- BANPROXY_INITALSIZE - default: 25
- BANPROXY_MAXTOTAL - default: 400
- BANPROXY_MAXIDLE - default: -1
- BANPROXY_MAXWAIT - default: 30000  
- BANSSUSER_USERNAME - default: ban_ss_user
- BANSSUSER_PASSWORD - default: password
- BANSSUSER_INITALSIZE - default: 25
- BANSSUSER_MAXTOTAL - default: 400
- BANSSUSER_MAXIDLE - default: -1
- BANSSUSER_MAXWAIT - default: 30000
- CAS_URL - default: https://cas.local.com/cas
- BANNER9_URL - default: https://banner9.school.edu
