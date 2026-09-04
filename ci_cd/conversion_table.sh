#!/bin/bash
#
# App name conversion table. Needed by multiple CI/CD scripts, so extracted into a shared script that can be sourced.
#
# Also functions as a list of apps which we manage.
#

declare -A APP_MAPPING

APP_MAPPING[applicationnavigator]="application-navigator"
APP_MAPPING[banneraccessmgmt]="access-management"
APP_MAPPING[banneradmin]="admin"
APP_MAPPING[bannereventpublisher]="event-publisher"
APP_MAPPING[bannerextensibility]="extensibility"
APP_MAPPING[bannergeneralssb]="ssb-general"
APP_MAPPING[brim]="brim"
APP_MAPPING[banneradminbpapi]="bpapi"
APP_MAPPING[communicationmanagement]="communicationmanagement"
APP_MAPPING[documentmanagementapi]="api-documentmanagement"
APP_MAPPING[employeeselfservice]="ss-employee"
APP_MAPPING[etranscriptapi]="api-etranscript"
APP_MAPPING[facultyselfservice]="ss-faculty"
APP_MAPPING[financeselfservice]="ss-finance"
APP_MAPPING[integrationapi]="api-integration"
APP_MAPPING[studentapi]="api-student"
APP_MAPPING[studentregistrationssb]="ssb-registration"
APP_MAPPING[studentselfservice]="ss-student"
APP_MAPPING[ethosmanagementcenter]="ethos-management"
APP_MAPPING[ellucianmessagingadapter]="messaging-adapter"

declare -A REVERSE_APP_MAPPING

# Build a reverse mapping to make it easier to look up the lowercased official name from our name.
for app in "${!APP_MAPPING[@]}"; do
	REVERSE_APP_MAPPING[${APP_MAPPING[$app]}]="$app"
done
