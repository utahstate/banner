# Bandock Banner 9 Self Service Base

## Tags
tomcat8-jre8-alpine
tomcat8-oraclejava8-oraclelinux7

## Env Variables
Environment variables are used to setup the base parameters for tomcat to connect to the database. They are set as environment variables so that the base image can be used in different Banner instances.  

### Defaults
If an environment variable is not specified at runtime then the defaults for that variable will be used.  At a minimum BANNERDB_JDBC, BANPROXY_PASSWORD, BANSSUSER_PASSWORD, CAS_URL and BANNER9_URL need to be set.  

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
