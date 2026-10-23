# Job Notifier Java API

A simple Java REST API for job notifications using Javalin, SQLite, and Docker.

> **Interview candidates:** AI assistance rules for this exercise are defined in
> [`AGENTS.md`](../AGENTS.md) at the repository root. Please read it (and ensure your
> AI assistant follows it) before you start.

## Features

- REST API endpoint for creating/saving jobs
- SQLite database for data persistence
- Docker containerization (Nginx + embedded Javalin/Jetty server)
- Javalin for lightweight routing
- Manual OpenAPI documentation
- Compiled classes are picked up automatically (no container restart needed)

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
  "name": "Senior Java Developer",
  "tags": [
    "java",
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
  -d '{"name":"Senior Java Developer","tags":["java","backend"],"salary":100000}'
```

## Project Structure

```
.
├── Dockerfile              # Java (Maven + JDK) Docker image configuration
├── docker-compose.yml      # Docker Compose orchestration
├── pom.xml                 # Maven project & dependencies
├── nginx/                  # Nginx configuration
├── public/                 # Application public directory (docs, generated openapi.yaml)
├── src/main/java/          # Application source code (domain, dao, service, controller, config)
├── src/test/java/          # JUnit test cases
├── data/                   # SQLite database storage (mounted volume)
└── target/                 # Compiled classes & dependencies (generated, mounted volume)
```

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

### Live recompilation without restarting the container

The whole `java/` directory (source + `pom.xml`) is bind-mounted into the `app`
container, and `target/` (build output) lives in its own named volume so it
survives container recreation. `docker-entrypoint.sh` starts a background
watcher (`inotifywait`) on `target/classes`: whenever fresh `.class` files show
up there, it kills and relaunches the Java process — the container itself is
never restarted.

This means the workflow to see a code change is:

```bash
# after editing files under java/src
docker compose exec app mvn compile
```

The running app restarts in place within a couple of seconds. There is no need
to run `docker compose restart` or `docker compose up --build` for ordinary
code changes — only `pom.xml`/dependency changes require a rebuild.

### Running tests

```bash
docker compose exec app mvn test
```

To install dependencies and build locally (without Docker), you need a JDK 21+
and Maven installed:

```bash
mvn -f java/pom.xml package
```

To run the application locally (without Docker):

```bash
mvn -f java/pom.xml compile
mvn -f java/pom.xml exec:java
```

## Troubleshooting

### Port Conflicts

If port 8082 is already in use, modify the port mapping in `docker-compose.yml` for the `nginx` service:

```yaml
  nginx:
    image: nginx:alpine
    ports:
      - "8086:80"  # Change 8082 to your preferred port
```

## License

This project is provided as-is for educational and interview purposes.
