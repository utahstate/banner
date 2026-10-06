#!/bin/bash
#
# Because Ellucian does not offer a health check endpoint.
#
# The Ellucian Messaging Adapter application returns JSON 404 errors, while Tomcat returns HTML, so we can tell if the adapter is running
# by checking the Content-Type header of the application's root with a HEAD request. cURL doesn't strip the carriage return character
# (hex 0x0d) when printing to the console, so that's why we're using $-strings and the \015

if [[ $(curl localhost:8080/EllucianMessagingAdapter/ -I | awk '/^Content-Type:/ {print $2}') == $'application/json\x0d' ]]; then
	exit 0
else
	exit 1
fi
