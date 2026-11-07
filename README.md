# New Docker container images for Banner 9 at USU

Building this because the old Banner 9 Dockerfiles are very repetitive and don't handle configuration in a particularly composable way.

This should improve runtime RAM costs, image build times, and final image sizes on top of improving maintainability for the future.

## Design Principles

- **Don't Repeat Yourself**
  - All images should build off of a single base image. This image should contain all common dependencies.
  - If a subset of containers share additional dependencies, they should build off a base image as well.
- **Composable Configuration**
  - Configuration should be handled in a "composable" manner. That is, it should be possible to share common configuration across multiple apps, and multiple configuration files should be combined at runtime to produce a single config.
  - Later definitions of the same key should override prior definitions.
  - Environment variables should take precedence over config files, but should generally not be used in production -- config files can be tracked in version control.
- **Follow Docker Best Practices**
  - Run pre-build setup in a build stage rather than an external script
  - Minimize and combine layers when possible
  - Don't include cache data in layers

# App Configuration

## Database Connections

Each database connection the app makes needs a username and password. In Kubernetes, these should be stored in Kubernetes Secret objects, mounted as volumes under `/run/passwords/db-$namespace` where `$namespace` is the property namespace it is configured under. Most apps require configuration for the following namespaces at a minimum:
- `banproxy`
- `banssuser`

The username is read from `/run/passwords/db-$namespace/username`, and the password from `/run/passwords/db-$namespace/password`. Additional parameters can be mounted at `/run/app-config/database.properties.d/$namespace.properties`. The namespace will automatically be added, so just the leaf property names should be given. If not provided, default parameters will be loaded from `/etc/default-database-connection.properties`, and a suitable default for this file is built into the base image.

## Other JavaProps

Additional arbitrary Tomcat server properties are loaded in from multiple places. In order of precedence:
- Command-line arguments of the form `-p <property>`/`--prop=<property>`
  - `<property>` should be replaced with the full property declaration (`path.to.prop=value`)
- Environment variables prefixed with `BANNER_` are converted to properties
  - The prefix is stripped, variable name lowercased, and underscores replaced with dots.
- Drop-in `*.properties` files located in `/run/app-config/properties.d/`

## Groovy Files

A common `banner_configuration.groovy` file must be provided under `/run/app-config/groovy/`, as well as an app-specific `${APP_NAME}_configuration.groovy`. In Kubernetes, these would typically be provided by mounting a ConfigMap as a volume.

If the `${APP_NAME}_configuration.groovy` file is not provided, the version which is shipped by Ellucian will be used instead. No default is provided for the common `banner_configuration.groovy` file.

## SAML Configuration

> [!IMPORTANT]
> SAML configuration is loaded based on the `${APP_NAME}_configuration.groovy` script mentioned above. The instructions below are based on the Groovy scripts contained in [this repository](./config). Modified Groovy scripts may require a different approach to SAML configuration.

A Java keystore file containing SAML secrets must be provided at `/saml/keystore/keystore.jks`. The password for this keystore will be read from `/saml/keystore/password`.

Additionally, metadata XML files must be provided at `/saml/metadata/idp.xml` and `/saml/metadata/sp.xml`.

## Odd Apps

Two applications, `BannerAdmin` and `BannerAccessMgmt`, have a structure which differs substantially from the others. They have no Groovy files, instead taking all of their configuration from app-specific `config.properties` files located under `webapps/BannerAdmin.ws/WEB-INF/classes` and `webapps/BannerAccessMgmt.ws/WEB-INF/classes` respectively. These files are patched during the image build process to enable SAML support, and the Java keystore passphrase is patched in at runtime. Symlinks are put in place to keep the runtime configuration as similar to other apps as possible.

# App Debugging

The startup script provides a number of ways to debug broken apps. In normal operation, if an error is encountered during execution of the script, the offending command will be logged to the console (via the `$BASH_COMMAND` variable). This variable contains the command as written, prior to parameter expansion, so secrets contained in shell variables are not written to the console. If the container is run with standard input attached to a terminal, failure of the script will additionally spawn an interactive shell to allow the administrator to investigate what went wrong.

If more in-depth debugging is required, the environment variable `DEBUG_STARTUP_SCRIPT_AND_LOG_PASSWORDS` can be set to enable the `-x` shell option, logging all commands to the console as run. As suggested by the variable name, however, this will result in any secrets processed by the startup script being logged to the console, as `-x` prints commands as executed, after parameter expansion (contrary to the above). Thus, any sensitive information should not be provided when running the application in this manner.
