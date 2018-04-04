# Bandock Banner 9 Admin Base
Banner 9 Admin Base is a base image to build the Banner Admin application on. Variables can be added from ENV variables, config file or docker secrets.

## Tags

https://hub.docker.com/r/edurepo/banner9-admin/

Tomcat8

- tomcat8-jre8-alpine
- tomcat8-oraclejava8-oracelinux7

## How to use

This base image is designed to have Banner 9 admin applications built on it. It is built for those applciations that require banproxy and not ban_ss_user.  Multiple builds are available.  At a minimum both builds of alpine and oracle linux will be available.  Alpine for its small size and oracle linux for Banner support compliance.

Properties can be loaded from multiple sources: environment variables, docker secrets, config file or combination of sources. They are appended to catalina.properties before tomcat starts. For tomcat to function the following properties need to be set: bannerdb.jdbc, banproxy.username, banproxy.username, banproxy.initialsize, banproxy.maxtotal, banproxy.maxidle, banproxy.maxwait, cas.url, banner9.url and theme.url. If they are not configued default values will be set from environment variables from the Dockerfile.

Uncompress the three banner admin wars into the same directory as the Dockerfile, as folders called BannerAdmin, BannerAdmin.ws and bannerHelp.

#### DockerFile

```Dockerfile
FROM edurepo/banner9-admin:tomcat8-jre8-alpine 

COPY --chown=tomcat:tomcat BannerAdmin /usr/local/tomcat/webapps/BannerAdmin
COPY --chown=tomcat:tomcat BannerAdmin.ws /usr/local/tomcat/webapps/BannerAdmin.ws
COPY --chown=tomcat:tomcat bannerHelp /usr/local/tomcat/webapps/bannerHelp
```

#### Build

```Shell
docker build -t registry.myschool.edu/banner/banneradmin:9.3.10.0.3
```

#### Run with environment variables

```Shell
docker run -e "BANNERDB_JDBC=jdbc:oracle:thin:@//oracle.example.edu:1521/prod" -e "BANPROXY_PASSWORD=password" -e "CAS_URL=https://cas.local.com/cas" -e "BANNER9_URL=https://banner9.school.edu" -e "THEME_URL=https://banner9.school.edu/BannerExtensibility/theme/getTheme?name=dev&amp;template=admin" -e "TIMEZONE=America/Denver" -p 8080:8080 registry.myschool.edu/banner/banneradmin:9.3.10.0.3
```

#### Run with config file

```Shell
docker run -e "CONFIG_FILE=/opt/config/banner.properties" -e "TIMEZONE=America/Denver" -p 8080:8080 registry.myschool.edu/banner/banneradmin:9.3.10.0.3
```

## Properties from Environment Variables

Environment variables are used to setup the base parameters for tomcat to connect to the database.

### Defaults

If an environment variable is not specificed at runtime then the defaults for the variable will be used. At a minimum BANNERDB_JDBC, BANPROXY_PASSWORD, CAS_URL, and BANNER9_URL need to be set. It is highly recommended setting the TIMEZONE to the timezone of your Banner database.

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
