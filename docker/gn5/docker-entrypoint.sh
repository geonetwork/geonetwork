#!/bin/bash
set -e

# Set
umask 002

# If arguments are passed, run them; otherwise run the default Java command
if [ "$#" -eq 0 ]; then
    exec java -jar app.jar
else
    exec "$@"
fi