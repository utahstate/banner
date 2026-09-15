#!/bin/bash
#
# Return the specified database environment to production mode.
#
# Usage: exit-maintenance.sh <database> [KUBECTL ARGS]
#
# Scales containers up to their runtime replica counts in the specified environment.
#
# Only touches deployments which use the database given (e.g. ZPROD).
#

kubectl get deployments -o json -l db.usu.edu/${1,,} "${@:2}" | jq '.items[].metadata | .name + " " + .annotations["banner.usu.edu/desired-replicas"]' -r | while read deployment replicas; do
	kubectl scale deployment "${deployment}" --replicas "${replicas}" "${@:2}"
done
