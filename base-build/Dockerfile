FROM amazoncorretto:17

# Simple base image for build stages with common dependencies already installed. Meant to shorten build times.

RUN --mount=type=cache,target=/var/cache,sharing=locked <<EOF
    set -e
    yum update -y
    amazon-linux-extras install epel
    yum install -y xmlstarlet shadow-utils git openssh tar gzip zip
    ln -sf /usr/share/zoneinfo/America/Denver /etc/localtime
    mkdir -p /opt/build/ ~/.ssh/
# Scan the build server keys into the known hosts file. If these change after this base image is built, that is a bad sign and builds should fail until it is resolved.
		ssh-keyscan build.banner.usu.edu >> ~/.ssh/known_hosts
EOF

WORKDIR /opt/build/

CMD ["bash"]
