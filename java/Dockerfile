FROM maven:3.9-eclipse-temurin-21

# inotify-tools provides inotifywait, used by docker-entrypoint.sh to detect
# freshly compiled classes and restart the app without restarting the container
RUN apt-get update && apt-get install -y --no-install-recommends \
    inotify-tools \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Warm the local Maven repo and produce an initial build so the very first
# `docker compose up` doesn't need to hit the network again at runtime.
COPY pom.xml ./
RUN mvn -q -o dependency:go-offline || mvn -q dependency:go-offline

COPY . .
RUN mvn -q package -DskipTests

EXPOSE 7000

ENTRYPOINT ["/app/docker-entrypoint.sh"]
CMD ["run"]
