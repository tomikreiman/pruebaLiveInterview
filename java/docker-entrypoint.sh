#!/bin/bash
set -e

cd /app
mkdir -p /app/data

CLASSPATH="target/classes:target/dependency/*"
APP_PID=""

start_app() {
    java -cp "$CLASSPATH" com.avature.jobnotifier.Main &
    APP_PID=$!
    echo "[entrypoint] app started (pid $APP_PID)"
}

stop_app() {
    if [ -n "$APP_PID" ] && kill -0 "$APP_PID" 2>/dev/null; then
        kill "$APP_PID" 2>/dev/null || true
        wait "$APP_PID" 2>/dev/null || true
    fi
}

restart_app() {
    echo "[entrypoint] recompiled classes detected, restarting app..."
    stop_app
    start_app
}

trap 'stop_app; exit 0' TERM INT

# Make sure the classpath reflects whatever is bind-mounted from the host
# (pom.xml / src) rather than only what was baked into the image at build time.
mvn -q -o compile 2>/dev/null || mvn -q compile
mvn -q -o dependency:copy-dependencies -DoutputDirectory=target/dependency -DoverWriteIfNewer=true 2>/dev/null \
    || mvn -q dependency:copy-dependencies -DoutputDirectory=target/dependency -DoverWriteIfNewer=true

start_app

if [ "$1" = "run" ]; then
    # Watch the compiled output. Whenever `mvn compile` is re-run (e.g. via
    # `docker compose exec app mvn compile` after editing source on the host),
    # the .class files under target/classes change and the running app is
    # restarted in place -- no `docker compose restart` needed.
    while true; do
        inotifywait -q -r -e close_write,create,delete,moved_to target/classes >/dev/null 2>&1 || sleep 1
        # Debounce: wait briefly for the whole compile output to settle.
        sleep 0.5
        restart_app
    done
else
    stop_app
    exec "$@"
fi
