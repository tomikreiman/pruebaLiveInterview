using System.Text.Json;
using Avature.JobNotifier.Domain;
using Avature.JobNotifier.Service;
using Microsoft.Data.Sqlite;
using JobNotifierService = Avature.JobNotifier.Service.JobNotifier;

namespace Avature.JobNotifier.Controller;

public class JobController
{
    private readonly JobService _jobService;
    private readonly JobNotifierService _jobNotifier;

    public JobController(JobService jobService, JobNotifierService jobNotifier)
    {
        _jobService = jobService;
        _jobNotifier = jobNotifier;
    }

    public async Task<IResult> CreateJob(HttpRequest request)
    {
        Dictionary<string, JsonElement> data;
        try
        {
            data = await JsonSerializer.DeserializeAsync<Dictionary<string, JsonElement>>(request.Body) ?? new();
        }
        catch (JsonException)
        {
            data = new();
        }

        try
        {
            if (!data.ContainsKey("name"))
            {
                throw new ArgumentException("Name is required");
            }

            var name = data["name"].ValueKind == JsonValueKind.String
                ? data["name"].GetString()
                : data["name"].ToString();
            var tags = data.TryGetValue("tags", out var tagsElement)
                ? JsonSerializer.Deserialize<List<string>>(tagsElement.GetRawText())!
                : new List<string>();
            var salary = !data.TryGetValue("salary", out var salaryElement) || salaryElement.ValueKind == JsonValueKind.Null
                ? 0
                : salaryElement.GetInt32();

            var job = new Job(name, tags, salary);

            var jobId = _jobService.CreateJob(job);

            _jobNotifier.Notify(job);

            return Results.Json(new Dictionary<string, object>
            {
                ["success"] = true,
                ["job_id"] = jobId,
                ["message"] = "Job created successfully"
            }, statusCode: StatusCodes.Status201Created);
        }
        catch (ArgumentException exception)
        {
            return Results.Json(
                new Dictionary<string, string> { ["error"] = exception.Message },
                statusCode: StatusCodes.Status400BadRequest);
        }
        catch (SqliteException exception)
        {
            return Results.Json(
                new Dictionary<string, string> { ["error"] = "Failed to create job: " + exception.Message },
                statusCode: StatusCodes.Status500InternalServerError);
        }
    }
}
