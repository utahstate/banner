#!/bin/bash
#
# Scale down all containers that use the given database.
#
# Usage: scale-down.sh [--dry-run] [--kubectl-context <context>] <database>
#
# This will scale all deployments that use the specified database to 0.
#

set -euo pipefail
dry_run=0

while [ $# -gt 0 ]; do
	case $1 in
	--dry-run)
		# Simply list the deployments that would be affected
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
		database="$1"
		;;
	esac
	shift
done

kubectl $([[ $dry_run -eq 0 ]] && echo scale --replicas=0 || echo get) deployment -l db.usu.edu/${database,,}=true --all-namespaces $([[ ${context+x} == x ]] && echo --context ${context})
