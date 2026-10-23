import os

import sys

project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
if project_root not in sys.path:
    sys.path.insert(0, project_root)


def generate_openapi() -> str:
    return """openapi: 3.0.0
info:
  title: Job Notifier API
  description: Simple Python REST API for job notifications
  version: 1.0.0
servers:
  - url: http://localhost:8082
    description: Local development server
paths:
  /api/jobs:
    post:
      summary: Create a new job
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              required:
                - name
              properties:
                name:
                  type: string
                tags:
                  type: array
                  items:
                    type: string
                salary:
                  type: integer
      responses:
        '201':
          description: Job created successfully
          content:
            application/json:
              schema:
                type: object
                properties:
                  success:
                    type: boolean
                  job_id:
                    type: integer
                  message:
                    type: string
        '400':
          description: Bad request (e.g. Title is required)
          content:
            application/json:
              schema:
                type: object
                properties:
                  error:
                    type: string
        '500':
          description: Internal server error
          content:
            application/json:
              schema:
                type: object
                properties:
                  error:
                    type: string
"""


def main() -> None:
    output_path = os.path.join(project_root, "public", "openapi.yaml")
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(generate_openapi())
    print(f"OpenAPI spec generated at {output_path}")


if __name__ == "__main__":
    main()
