FROM harbor.usu.edu/banner/base-bannerselfservice:oraclelinux6-tomcat8-java8
MAINTAINER "Eric Allen <eric.allen@usu.edu>"

ENV BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=false \
    TIMEZONE=America\Denver


COPY BannerGeneralSsb /usr/local/tomcat/webapps/BannerGeneralSsb
