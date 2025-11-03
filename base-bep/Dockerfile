FROM usuit/banner:base-9-jdk17-corretto

LABEL edu.usu.banner.image="base-bep"

ENV CDCADMIN_USERNAME=cdcadmin \
    CDCADMIN_INITALSIZE=25 \
    CDCADMIN_MAXTOTAL=300 \
    CDCADMIN_MAXIDLE=-1 \
    CDCADMIN_MAXWAIT=30000

# Copy in the BEP run.sh
COPY --chmod=755 run.sh /usr/local/tomcat/bin

# Expose extra port BEP uses.
EXPOSE 9010

# Set up the common arguments
ONBUILD ARG environment
