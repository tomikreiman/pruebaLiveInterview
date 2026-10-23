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
                  /api/jobs/stats:
                    get:
                      summary: Salary and tag statistics across all jobs
                      responses:
                        '200':
                          description: Aggregated job statistics
                          content:
                            application/json:
                              schema:
                                type: object
                                properties:
                                  total_jobs:
                                    type: integer
                                  average_salary:
                                    type: integer
                                  median_salary:
                                    type: integer
                                  count_by_tag:
                                    type: object
                                    additionalProperties:
                                      type: integer
                                  salary_bands:
                                    type: array
                                    items:
                                      type: object
                                      properties:
                                        range:
                                          type: string
                                        count:
                                          type: integer
                  /api/jobs/schedule:
                    get:
                      summary: Maximum set of non-overlapping interview/event slots
                      description: >-
                        Each job has an interview/event slot [start_hour, end_hour) in 24-hour
                        format that occupies a single shared resource (one room/interviewer).
                        Returns the largest set of jobs whose slots do not overlap.
                      responses:
                        '200':
                          description: The selected non-overlapping interview/event slots
                          content:
                            application/json:
                              schema:
                                type: object
                                properties:
                                  selected_count:
                                    type: integer
                                    description: Number of non-overlapping slots selected
                                  selected:
                                    type: array
                                    items:
                                      type: object
                                      properties:
                                        name:
                                          type: string
                                        start_hour:
                                          type: integer
                                          description: Slot start hour, inclusive (0-23, 24h format)
                                        end_hour:
                                          type: integer
                                          description: Slot end hour, exclusive (1-24, 24h format)
                """;
    }
}
