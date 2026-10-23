using Avature.JobNotifier.Controller;
using Microsoft.AspNetCore.StaticFiles;

namespace Avature.JobNotifier.Config;

public static class Routes
{
    private static readonly FileExtensionContentTypeProvider ContentTypeProvider = new();

    public static void Register(WebApplication app, Container container, string openApiPath)
    {
        var homeController = container.Get<HomeController>("home_controller");
        var jobController = container.Get<JobController>("job_controller");

        app.MapGet("/", homeController.Index);

        app.MapGet("/openapi.yaml", (HttpContext context) =>
        {
            if (!File.Exists(openApiPath))
            {
                return Results.NotFound();
            }

            context.Response.Headers.CacheControl = "no-store";
            return (IResult)Results.File(Path.GetFullPath(openApiPath), "application/yaml");
        });

        app.MapGet("/docs/{path}", (string path, HttpContext context) =>
        {
            var file = Path.Combine("public", "docs", path);
            if (!File.Exists(file))
            {
                return Results.NotFound();
            }

            context.Response.Headers.CacheControl = "no-store";
            ContentTypeProvider.TryGetContentType(file, out var contentType);
            return (IResult)Results.File(Path.GetFullPath(file), contentType ?? "application/octet-stream");
        });

        app.MapPost("/api/jobs", jobController.CreateJob);
    }
}
