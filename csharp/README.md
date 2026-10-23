# Job Notifier C# API

A simple C# REST API for job notifications using ASP.NET Core, SQLite, and Docker.

> **Interview candidates:** AI assistance rules for this exercise are defined in
> [`AGENTS.md`](../AGENTS.md) at the repository root. Please read it (and ensure your
> AI assistant follows it) before you start.

## Features

- REST API endpoint for creating/saving jobs
- SQLite database for data persistence
- Docker containerization (Kestrel serves HTTP directly, no reverse proxy)
- ASP.NET Core for lightweight routing
- Manual OpenAPI documentation
- Source changes are picked up automatically (no container restart needed)

## Requirements

- Docker
- Docker Compose

## Installation

1. Clone the repository
2. Build and start the Docker container:

```bash
docker compose up -d --build
```

The API will be available at `http://localhost:8082`
OpenAPI UI is available at `http://localhost:8082/docs/index.html`

## API Usage

### Create a new job

**Endpoint:** `POST /api/jobs`

**Request Body:**
```json
{
  "name": "Senior C# Developer",
  "tags": [
    "csharp",
    "backend"
  ],
  "salary": 100000
}
```

**Required Fields:**
- `name` (string)

**Optional Fields:**
- `tags` (array of strings)
- `salary` (integer)

**Response:**
```json
{
  "success": true,
  "job_id": 1,
  "message": "Job created successfully"
}
```

**Example using curl:**
```bash
curl -X POST http://localhost:8082/api/jobs \
  -H "Content-Type: application/json" \
  -d '{"name":"Senior C# Developer","tags":["csharp","backend"],"salary":100000}'
```

## Project Structure

```
.
├── Dockerfile              # .NET SDK Docker image configuration
├── docker-compose.yml      # Docker Compose orchestration
├── JobNotifier.sln         # .NET solution
├── public/                 # Application public directory (docs, generated openapi.yaml)
├── src/JobNotifier/        # Application source code (Domain, Dao, Service, Controller, Config)
├── tests/JobNotifier.Tests # xUnit test cases
├── data/                   # SQLite database storage (mounted volume)
└── /tmp/job-notifier-build # Compiled output inside the app container
```

### Why there is no Nginx container here

The PHP variant needs Nginx because PHP-FPM speaks FastCGI, not HTTP, so
something has to translate incoming HTTP requests for it. The Python and Java
variants keep Nginx mainly for consistency across stacks even though their app
servers (uWSGI, embedded Jetty) can already speak HTTP. Kestrel (ASP.NET Core's
server) also serves HTTP natively, and this app doesn't need Nginx for TLS
termination, static-file offloading, or multiplexing multiple upstream apps
behind one port — so the `app` service is published straight to the host
(`8082:7000`) and there is no separate reverse-proxy container to build, wire
up, or keep healthy.

## Database Schema

The SQLite database contains a `jobs` table with the following structure:

```sql
CREATE TABLE jobs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    tags TEXT,
    salary INTEGER,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
)
```

And a `people` table for notifications:

```sql
CREATE TABLE people (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    personal_tag TEXT NOT NULL,
    mail TEXT,
    phone TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
)
```

## Development

### Live rebuild without restarting the container

The whole `csharp/` directory is bind-mounted read-only into the `app` container.
`dotnet watch` monitors the source and rebuilds the application into a writable
container-local directory (`/tmp/job-notifier-build`, outside the read-only mount)
whenever a source file changes, then restarts the process in place — the
container itself is never restarted.

This means the workflow to see a code change is just:

```bash
# edit files under csharp/src, then wait a couple of seconds
```

There is no manual compile/restart step (unlike the Java variant's
`docker compose exec app mvn compile`). Hot Reload is intentionally disabled
(`--no-hot-reload`): most non-trivial edits (new members, `const` fields, method
signatures, etc.) are "rude edits" that Hot Reload cannot apply in place, and it
would otherwise prompt on the console to fall back to a restart — a prompt there
is no TTY to answer inside the container, so the app would silently keep running
the stale build. Disabling Hot Reload makes every change go through the same
deterministic rebuild-and-restart, so edits are never missed.

### Running tests

```bash
docker compose exec app dotnet test
```

To restore dependencies and build locally (without Docker), you need the .NET 8 SDK:

```bash
dotnet build csharp/JobNotifier.sln
```

To run the application locally (without Docker):

```bash
cd csharp
dotnet run --project src/JobNotifier/JobNotifier.csproj
```

## Troubleshooting

### Port Conflicts

If port 8082 is already in use, modify the port mapping in `docker-compose.yml` for the `app` service:

```yaml
  app:
    ports:
      - "8086:7000"  # Change 8082 to your preferred port
```

## License

This project is provided as-is for educational and interview purposes.
