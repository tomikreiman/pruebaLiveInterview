#!/bin/bash
set -e

mkdir -p /var/www/html/data

composer install --optimize-autoloader --no-interaction

php /var/www/html/scripts/init-db.php

chown -R www-data:www-data /var/www/html/data

php /var/www/html/vendor/bin/openapi /var/www/html/src -o /var/www/html/public/openapi.yaml

exec "$@"
