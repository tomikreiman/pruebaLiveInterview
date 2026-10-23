package com.avature.jobnotifier.scripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class GenerateOpenApi {
    private GenerateOpenApi() {
    }

    public static void main(String[] args) throws IOException {
        run("public/openapi.yaml");
    }

    public static void run(String outputPath) throws IOException {
        Path path = Path.of(outputPath);
        File parent = path.toFile().getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        Files.writeString(path, spec());
        System.out.println("OpenAPI spec generated at " + path.toAbsolutePath());
    }

    private static String spec() {
        return """
                openapi: 3.0.0
                info:
                  title: Job Notifier API
                  description: Simple Java REST API for job notifications
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
                """;
    }
}
