FROM php:8.1-fpm

# Install dependencies
RUN apt-get update && apt-get install -y \
    libonig5 \
    libsqlite3-0 \
    libsqlite3-dev \
    libzip-dev \
    libxml2-dev \
    && rm -rf /var/lib/apt/lists/*

# Install PHP extensions
RUN docker-php-ext-install pdo pdo_sqlite zip dom

# Set working directory
WORKDIR /var/www/html

# Install composer
RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer

# Expose port 9000 for PHP-FPM
EXPOSE 9000

# Start PHP-FPM
ENTRYPOINT ["/var/www/html/docker-entrypoint.sh"]
CMD ["php-fpm"]
