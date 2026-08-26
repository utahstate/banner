#!/bin/bash
#
# Usage: update-groovy.sh <environment>
#
# Updates Groovy configuration scripts in Kubernetes for the given environment. Requires an appropriate KUBECONFIG environment variable to be set.
#
# Adds all Groovy files matching ../config/<environment>/*.groovy
#

set -euo pipefail

environment="$1"

args=()

# Collect files
for file in ../config/"${environment}"/*.groovy; do
	if [[ "${file}" == ../config/"${environment}"/'*.groovy' ]]; then
		echo "Error: no Groovy scripts found, aborting..."
		exit 1
	fi
	args+=("--from-file=$(basename "$file")=$file")
done

# Create ConfigMap in Kubernetes
kubectl create --dry-run=client -o yaml -n "${environment}" configmap "groovy-configs" "${args[@]}" | kubectl apply -f -
