ARG jdk=21
FROM amazoncorretto:${jdk}

# Simple base image for build stages with common dependencies already installed. Meant to shorten build times. Provides a convenience script download-wars.

RUN --mount=type=cache,target=/var/cache,sharing=locked <<EOF
    set -e
    dnf update -y
    dnf install -y shadow-utils git openssh tar gzip zip unzip
    ln -sf /usr/share/zoneinfo/America/Denver /etc/localtime
    mkdir -p /opt/build/ ~/.ssh/
EOF

COPY --chmod=644 ssh-config /root/.ssh/config
COPY --chmod=644 build-host-keys /root/.ssh/known_hosts
COPY --chmod=755 download-wars.sh /usr/local/bin/download-wars

WORKDIR /opt/build/
ENV BUILD_DIR=/opt/build/

ONBUILD ARG VERSION INSTANCE

CMD ["bash"]
