using System.Runtime.CompilerServices;
using Avature.JobNotifier.Config;
using Avature.JobNotifier.Scripts;

Directory.SetCurrentDirectory(RepoRoot());

var dbPath = Environment.GetEnvironmentVariable("DB_PATH") ?? Path.Combine("data", "jobs.db");
var openApiPath = Environment.GetEnvironmentVariable("OPENAPI_PATH") ?? Path.Combine("public", "openapi.yaml");
InitDb.Run(dbPath);
GenerateOpenApi.Run(openApiPath);

var container = new Container();
Dependencies.Configure(container);

var builder = WebApplication.CreateBuilder(args);
var port = int.Parse(Environment.GetEnvironmentVariable("APP_PORT") ?? "7000");
builder.WebHost.UseUrls($"http://0.0.0.0:{port}");

var app = builder.Build();
Routes.Register(app, container, openApiPath);
app.Run();

static string RepoRoot([CallerFilePath] string sourceFile = "")
{
    return Path.GetFullPath(Path.Combine(Path.GetDirectoryName(sourceFile)!, "..", ".."));
}
