#!/bin/bash
#
# Usage: update-saml.sh <environment>
#
# Updates SAML metadata in Kubernetes for the given environment. Requires an appropriate KUBECONFIG environment variable to be set.
#
# Reads SAML metadata from ../config/<environment>/saml/ and expects files to have names of <APPNAME>/<sp|idp>.xml, where <APPNAME> is the name used in this repo.
# ConfigMaps will be created with the name saml-<APPNAME> in the namespace <environment>.
#

set -euo pipefail

environment="$1"

for app in ../config/"${environment}"/saml/*; do
	# This will patch the existing configmap rather than deleting and recreating it if one exists.
	kubectl create --dry-run=client -o yaml -n "${environment}" configmap "saml-$(basename "$app")" \
		--from-file=idp.xml="$app/idp.xml" \
		--from-file=sp.xml="$app/sp.xml" |
		kubectl apply -f -
done
