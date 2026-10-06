# USU Banner Container Images and Deployments

This repository contains build and deployment instructions for Banner 9 web applications and supporting microservices used at Utah State University.

## How to Use

The images are built with the assumption that the will be run in Kubernetes. As a result, some of the decisions may be at odds with the conventions of other container orchestration technologies, such as the expectation of many small files containing single configuration values, particularly for database connection secrets.

Properties are loaded from multiple sources and written to `catalina.properties` prior to starting Tomcat. In order of precedence, with later configuration taking priority over earlier:
- Database connection configuration
- Runtime dropin property files
- Environment variables
- Command-line arguments

### Database Connections

To set database connection properties, the containers will scan `/run/passwords` for directories of the form `db-$prefix`, where `$prefix` is the first component of the properties used to describe the database connection (e.g. `banproxy` and `banssuser` for self-service apps). If these directories contain `username` and `password` files, the contents of those files will be set as the `$prefix.username` and `$prefix.password` properties respectively.

The intended deploy mechanism is to store the username and password together in a Kubernetes secret and mount that secret into the containers.

The other connection properties (`initialsize`, `maxtotal`, `maxidle`, and `maxwait`) are then read from `/run/app-config/database.properties.d/$prefix.properties`, or, if that file doesn't exist, from `/etc/default-database-connection.properties`. The properties listed in these files should omit `$prefix.`, it will be added automatically.

The `default-database-connection.properties` file built into the images sets the following defaults:
- `initialsize`: 25
- `maxtotal`: 400
- `maxidle`: -1
- `maxwait`: 30000

### Runtime Property Dropins

Small property "drop-in" files can be supplied as `*.property` files under `/run/app-config/properties.d`. These files will be processed in lexical order, with later configuration taking precedence, so if multiple files are used, prefixing their names with numbers is recommended to control the order they are read in.

The intended deploy mechanism is to store one or more of these files in a Kubernetes ConfigMap object and mount that ConfigMap to `/run/app-config/properties.d`.

### Environment Variables

Various environment variables can be used to control the behavior of the applications both at startup and runtime. These broadly fall into two categories: properties and others.

#### Property Variables

Arbitrary properties can be set using environment variables, which will override any properties set in drop-in files or synthesized for database connections. Any environment variable prefixed with `BANNER_` will be converted to a property; this is done by stripping the `BANNER_` prefix, converting the name to lowercase, and replacing all remaining underscores with dots. For example, the environment variable `BANNER_BANNERDB_JDBC` would be converted to the property `bannerdb.jdbc`.

This is primarily intended for one-off changes that don't warrant a ConfigMap (for example, because they are only used by a single application). Generally, ConfigMaps should not be used to set environment variables for the purpose of setting properties; a drop-in file is a better way to handle this.

#### Other Variables

The containers understand a number of environment variables:
- `CATALINA_OPTS`: Contains command-line arguments to the Catalina process.
- `XMS`: Used to set the `-Xms` command-line argument when using the default `CATALINA_OPTS`. Defaults to `2g` in most containers
- `XMX`: Used to set the `-Xmx` command-line argument when using the default `CATALINA_OPTS`. Defaults to `4g` in most containers
- `MAXMETASPACE`: Used to set the `-XX:MaxMetaspaceSize` command-line argument when using the default `CATALINA_OPTS`. Defaults to `512m` in most containers
- `LOGGING_DIR`: The directory to write logs to. Defaults to `/usr/local/tomcat/logs`
- `TIMEZONE`: Sets the timezone; this should usually be set when building the base image, where it will link `/etc/localtime` as well, rather than at runtime.
- `DEBUG_STARTUP_SCRIPT_AND_LOG_PASSWORDS`: Causes the `-x` shell option to be set in the entrypoint script, printing commands as run to the console. As the name suggests, this will result in any passwords handled by shell variables in the script to be logged to the console, so care should be taken.

### Command-Line Options

In addition to the command to run (which defaults to the standard `catalina.sh run` for Tomcat containers), the containers understand the `--prop`/`-p` option to set a property. This should generally not be used in a production deployment, but for testing and development it can be used as an alternative to the more cumbersome methods of setting properties enumerated above.
