#!/bin/bash
set -e

cd /app
mkdir -p /app/data /tmp/job-notifier-build

dotnet restore JobNotifier.sln

exec "$@"
