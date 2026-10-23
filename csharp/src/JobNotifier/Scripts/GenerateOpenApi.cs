namespace Avature.JobNotifier.Scripts;

public static class GenerateOpenApi
{
    public static void Run(string outputPath)
    {
        var directory = Path.GetDirectoryName(outputPath);
        if (directory is not null)
        {
            Directory.CreateDirectory(directory);
        }

        File.WriteAllText(outputPath, Spec());
        Console.WriteLine($"OpenAPI spec generated at {Path.GetFullPath(outputPath)}");
    }

    private static string Spec()
    {
        return """
            openapi: 3.0.0
            info:
              title: Job Notifier API
              description: Simple C# REST API for job notifications
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
