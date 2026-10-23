# Job Notifier Python API

A simple Python REST API for job notifications using Flask, SQLite, and Docker.

> **Interview candidates:** AI assistance rules for this exercise are defined in
> [`AGENTS.md`](../AGENTS.md) at the repository root. Please read it (and ensure your
> AI assistant follows it) before you start.

## Features

- REST API endpoint for creating/saving jobs
- SQLite database for data persistence
- Docker containerization (Nginx + uWSGI)
- Flask for lightweight routing
- Manual OpenAPI documentation

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
  "name": "Senior Python Developer",
  "tags": [
    "python",
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
  -d '{"name":"Senior Python Developer","tags":["python","backend"],"salary":100000}'
```

## Project Structure

```
.
├── Dockerfile              # Python Docker image configuration
├── docker-compose.yml      # Docker Compose orchestration
├── requirements.txt        # Python dependencies
├── config/                 # DI Dependencies and Routes configuration
├── nginx/                  # Nginx configuration
├── public/                 # Application public directory (main.py, docs)
├── scripts/                # Database initialization scripts
├── src/                    # Application source code (Domain, Dao, Service, Controller)
├── tests/                  # pytest test cases
├── data/                   # SQLite database storage (mounted volume)
└── venv/                   # Virtual environment (generated)
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

To run the unit tests:

```bash
docker compose exec app pytest
```

To install dependencies locally (without Docker):

```bash
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

To run the application locally (without Docker):

```bash
python scripts/init_db.py
python public/main.py
```

## Troubleshooting

### Port Conflicts

If port 8082 is already in use, modify the port mapping in `docker-compose.yml` for the `nginx` service:

```yaml
  nginx:
    image: nginx:alpine
    ports:
      - "8083:80"  # Change 8082 to your preferred port
```

## License

This project is provided as-is for educational and interview purposes.
