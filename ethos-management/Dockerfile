FROM usuit/banner-base:build-jdk21-latest AS build
ARG VERSION ENVIRONMENT

RUN --mount=type=ssh download-wars "${ENVIRONMENT}" ethosapimanagementcenter

FROM usuit/banner-base:9-jdk21-tomcat10 AS final
ARG VERSION ENVIRONMENT

ENV ENVIRONMENT=${ENVIRONMENT} APP_NAME="ethosapimanagementcenter"
