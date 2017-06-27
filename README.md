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

  SSBENABLED = true
  SSBORACLEUSERSPROXIED = true
  SSBPASSWORD_RESET_ENABLED = true

  CLASSLISTAPP_FGEURL = http://host:port/StudentFacultyGradeEntry/
  CLASSLISTAPP_ATTRURL = http://host:port/FacultyAttendanceTrackingSsb/

  BANNER_SSO_AUTHENTICATIONPROVIDER = default
  BANNER_SSO_AUTHENTICATIONASSERTIONATTRIBUTE = UDC_IDENTIFIER
  GRAILS_PLUGIN_SPRINGSECURITY_CAS_ACTIVE = false
  GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERURLPREFIX = http://CAS_HOST:PORT/cas
  GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVICEURL = http://BANNER9_HOST:PORT/APP_NAME/j_spring_cas_security_check
  GRAILS_PLUGIN_SPRINGSECURITY_CAS_SERVERNAME = http://BANNER9_HOST:PORT
  GRAILS_PLUGIN_SPRINGSECURITY_CAS_PROXYCALLBACKURL = http://BANNER9_HOST:PORT/APP_NAME/secure/receptor

  GRAILS_PLUGIN_SPRINGSECURITY_LOGOUT_AFTERLOGOUTURL = https://cas-server/logout?url=http://myportal/main_page.html

  BANNERXE_URL_MAPPER_ADVISINGROOT = <protocol>://<host>:<port>/StudentAdvisorSSB
  BANNERXE_URL_MAPPER_DEGREEWORKSURL = <protocol>://<host>:<port>/dev/dwadvss/banmain/IRISLink.cgi?CAS=ENABLED&SERVICE=LOGON&SCRIPT=SD2WORKS&PORTALSTUID=<STUDENTID>

  FOOTERFADEAWAYTIME = 10000
