#!/bin/bash
set -eo pipefail

source copilot_apps.sh

if command -v tmux &>/dev/null; then
	tmux new-session -s banner-dr-teardown \; detach-client
	TMUX_AVAIL=1
fi

function teardown_app {
	local appname="$1"
	if [ ${TMUX_AVAIL:-0} -eq 1 ]; then
		tmux neww -t 'banner-dr-teardown:{end}' -a -n "${appname}" copilot svc delete --name "${appname}" --yes
	else
		copilot svc delete --name "${appname}" --yes
	fi
}
teardown_app static
for aws_appname in "${!apps[@]}"; do
	teardown_app "${aws_appname}"
done

if [ ${TMUX_AVAIL:-0} -eq 1 ]; then
	tmux killw -t 'banner-dr-teardown:0'
	tmux attach -t banner-dr-teardown
fi
