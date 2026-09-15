#!/bin/bash
#
# Place the specified database into maintenance mode.
#
# Usage: enter-mainenance.sh <database> [KUBECTL ARGS]
#
# This will scale all app containers that use the specified database to 0.
#

set -euo pipefail

kubectl scale deployment -l db.usu.edu/${1,,}=true --replicas=0 "${@:2}"
