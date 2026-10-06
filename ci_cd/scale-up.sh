#!/bin/bash
#
# Scale up all containers that use the given database.
#
# Usage: scale-up.sh [--dry-run] [--kubectl-context <context>] <database>
#
# Scales all deployments that use the specified database to their production scale
#

set -euo pipefail
dry_run=0

while [ $# -gt 0 ]; do
	case $1 in
	--dry-run)
		dry_run=1
		;;
	--kubectl-context)
		context="$2"
		shift
		;;
	--kubectl-context=*)
		context="${1#--kubectl-context=}"
		;;
	*)
		database="${1,,}"
		;;
	esac
	shift
done

if [[ ${database+x} == '' ]]; then
	echo "No database given!"
	exit 1
fi

kubectl get deployments -o json -l db.usu.edu/${database} --all-namespaces | jq '.items[].metadata | .name + " " + .namespace + " " + .annotations["banner.usu.edu/desired-replicas"]' -r | while read deployment namespace replicas; do
	if [[ $dry_run -eq 1 ]]; then
		echo "Would scale deployment ${deployment} in namespace ${namespace} to ${replicas}"
	else
		kubectl scale deployment "${deployment}" --replicas "${replicas}" --namespace "${namespace}"
	fi
done
