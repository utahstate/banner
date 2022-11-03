# Student Self Service

## Links
 - StudentSelfService/ssb/studentGrades
 - StudentSelfService/ssb/classListApp/classListPage
 - StudentSelfService/ssb/termSelection
 - StudentSelfService/ssb/studentProfile

## Banner Config
  ONLINEHELP_URL = http://HOST:PORT/banner9OH
  BANNER_TRANSACTIONTIMEOUT = 30
  BANNER8_SS_URL = http://HOST:PORT/DAD/
  EMAIL_BATCH_SIZE = 600
  BANNER_PICTURESPATH = /test/images

## Student Self Service

```
  SSBENABLED = true
  SSBORACLEUSERSPROXIED = true
  SSBPASSWORD_RESET_ENABLED = true

  CLASSLISTAPP_FGEURL = http://host:port/StudentFacultyGradeEntry/
  CLASSLISTAPP_ATTRURL = http://host:port/FacultyAttendanceTrackingSsb/

  CAS_URL=http://CAS_HOST:PORT/cas
  BANNER9_URL=http://BANNER9_HOST:PORT
  BANNER9_AFTERLOGOUTURL = https://cas-server/logout?url=http://myportal/main_page.html
  GRAILS_PLUGIN_SPRINGSECURITY_HOMEPAGEURL=http://<host:port>/StudentSelfService
  DEGREEWORKS_URL=<protocol>://<host>:<port>/dev/dwadvss/banmain/IRISLink.cgi?CAS=ENABLED&SERVICE=LOGON&SCRIPT=SD2WORKS&PORTALSTUID=<STUDENTID>
  BANNERXE_URL_MAPPER_ADVISINGROOT = <protocol>://<host>:<port>/StudentAdvisorSSB
  BANNERXE_URL_MAPPER_DEGREEWORKSURL = <protocol>://<host>:<port>/dev/dwadvss/banmain/IRISLink.cgi?CAS=ENABLED&SERVICE=LOGON&SCRIPT=SD2WORKS&PORTALSTUID=<STUDENTID>
  DEFAULTWEBSESSIONTIMEOUT=15000
  FOOTERFADEAWAYTIME = 10000

  BANNER_THEME_URL=http://BANNER9_HOST:PORT/StudentSelfService/ssb/theme
  BANNER_THEME_NAME=default
  BANNER_THEME_TEMPLATE=StudentSelfService
  BANNER_THEME_CACHETIMEOUT=120

  BANNER_ANALYSTICS_TRACKERID=
  BANNER_ANALYSTICS_ALLOWELLUCIANTRACKER=true
```