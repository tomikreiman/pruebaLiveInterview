FROM python:3.11-slim

# Set working directory
WORKDIR /var/www/html

# Install system dependencies
RUN apt-get update && apt-get install -y \
    gcc \
    libsqlite3-0 \
    && rm -rf /var/lib/apt/lists/*

# Expose port 5000 for the Flask app (uWSGI)
EXPOSE 5000

# Start the application
ENTRYPOINT ["/var/www/html/docker-entrypoint.sh"]
CMD ["uwsgi", "--ini", "/var/www/html/uwsgi.ini"]
