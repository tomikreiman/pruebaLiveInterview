#!/bin/bash
set -e

mkdir -p /var/www/html/data

python3 -m venv /var/www/html/.venv
/var/www/html/.venv/bin/pip install --no-cache-dir -r /var/www/html/requirements.txt
source /var/www/html/.venv/bin/activate

python3 /var/www/html/scripts/init_db.py
python3 /var/www/html/scripts/generate_openapi.py

exec "$@"
