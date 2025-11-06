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
  
