# Bandock Banner 9 Admin Base
Banner 9 Admin Base is a base image to build the Banner Admin application on. Variables can be added from ENV variables, config file or docker secrets.

## Tags

Tomcat8

- tomcat8-jre8-alpine, tomcat8-oraclejava8-oracelinux7

## How to use

These Dockerfiles/images are meant to the base container to add the Banner Admin application to.

Uncompress the three banner admin wars into the same directory as the Dockerfile, as folders called BannerAdmin, BannerAdmin.ws and bannerHelp.

#### DockerFile

```Dockerfile
FROM bandock/base-admin:tomcat8-jre8-alpine 

COPY BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY bannerHelp /usr/local/tomcat/webapps/bannerHelp
```

#### Build

```Shell
docker build -t bandock/banneradmin:9.3.10.0.3
```

#### Run with environment variables

```Shell
docker run -e "BANNERDB_JDBC=jdbc:oracle:thin:@//oracle.example.edu:1521/prod" -e "BANPROXY_PASSWORD=password" -e "CAS_URL=https://cas.local.com/cas" -e "BANNER9_URL=https://banner9.school.edu" -e "THEME_URL=https://banner9.school.edu/BannerExtensibility/theme/getTheme?name=dev&amp;template=admin" -e "TIMEZONE=America/Denver" -p 8080:8080 bandock/banneradmin:9.3.10.0.3
```

#### Run with config file

```Shell
docker run -e "CONFIG_FILE=/opt/config/banner.properties" -e "TIMEZONE=America/Denver" -p 8080:8080 bandock/banneradmin:9.3.10.0.3
```

## Properties from Environment Variables

Environment variables are used to setup the base parameters for tomcat to connect to the database.

### Defaults

If an environment variable is not specificed at runtime then the defaults for the variable will be used. At a minimum BANNERDB_JDBC, BANPROXY_PASSWORD, CAS_URL, and BANNER9_URL need to be set.

### Environment Variables

```Shell
TIMEZONE - default: America/New_York
XMS - default: 2g
XMX - default: 4g
BANNERDB_JDBC - default: jdbc:oracle:thin:@//oracle.example.edu:1521/prod
BANPROXY_USERNAME - default: banproxy
BANPROXY_PASSWORD - default: password
BANPROXY_INITALSIZE - default: 25
BANPROXY_MAXTOTAL - default: 400
BANPROXY_MAXIDLE - default: -1
BANPROXY_MAXWAIT - default: 30000
CAS_URL - example: https://cas.local.com/cas
BANNER9_URL - example: https://banner9.school.edu
THEME_URL - example: https://banner9.school.edu/BannerExtensibility/theme/getTheme?name=dev&amp;template=admin
```

(Note: If a THEME_URL is not supplied, themeing will not be enabled.)

## Properties from Config file

When loading from a config file, defaults are ignored and all parameters will need to be set in the file.

```Shell
bannerdb.jdbc=jdbc:oracle:thin:@//oracle.example.edu:1521/prod
banproxy.username=banproxy
banproxy.password=password
banproxy.initialsize=25
banproxy.maxtotal=400
banproxy.maxidle=-1
banproxy.maxwait=30000
cas.url=https://cas.local.com/cas
banner9.baseurl=https://banner9.school.edu
theme.url=https://banner9.school.edu/BannerExtensibility/theme/getTheme?name=dev&template=admin
```
