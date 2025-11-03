FROM usuit/banner:base-9-jdk17-corretto

LABEL edu.usu.banner.image="base-bcm"

# Set BCM-specific environment variables
ENV COMMMGR_USERNAME=commmgr \
    COMMMGR_INITALSIZE=25 \
    COMMMGR_MAXTOTAL=400 \
    COMMMGR_MAXIDLE=-1 \
    COMMMGR_MAXWAIT=30000

# Copy in the bcm run.sh
COPY --chmod=755 run.sh /usr/local/tomcat/bin

# Set up the common arguments
ONBUILD ARG environment
