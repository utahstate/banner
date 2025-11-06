ARG jdk=17
FROM amazoncorretto:${jdk}

# Simple base image for build stages with common dependencies already installed. Meant to shorten build times. Provides a convenience script download-wars.

RUN --mount=type=cache,target=/var/cache,sharing=locked <<EOF
    set -e
    yum update -y
    amazon-linux-extras install epel
    yum install -y xmlstarlet shadow-utils git openssh tar gzip zip unzip
    ln -sf /usr/share/zoneinfo/America/Denver /etc/localtime
    mkdir -p /opt/build/ ~/.ssh/
# Scan the build server keys into the known hosts file. If these change after this base image is built, that is a bad sign and builds should fail until it is resolved.
		ssh-keyscan build.banner.usu.edu >> ~/.ssh/known_hosts
EOF

COPY ssh-config /root/.ssh/config
COPY --chmod=755 download-wars.sh /usr/local/bin/download-wars

WORKDIR /opt/build/
ENV BUILD_DIR=/opt/build/

ONBUILD ARG version instance

CMD ["bash"]
