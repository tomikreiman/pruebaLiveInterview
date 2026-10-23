using Avature.JobNotifier.Controller;
using Avature.JobNotifier.Dao;
using Avature.JobNotifier.Service;
using Microsoft.Data.Sqlite;
using Microsoft.Extensions.Logging;
using JobNotifierService = Avature.JobNotifier.Service.JobNotifier;

namespace Avature.JobNotifier.Config;

public static class Dependencies
{
    public static void Configure(Container container)
    {
        container.Set("logger", _ => LoggerFactory
            .Create(builder => builder.AddConsole().SetMinimumLevel(LogLevel.Debug))
            .CreateLogger("job-notifier"));

        container.Set("connection", _ => CreateConnection());

        container.Set("mail_sender", c => new MailSender(c.Get<ILogger>("logger")));
        container.Set("sms_sender", c => new SmsSender(c.Get<ILogger>("logger")));

        container.Set("job_repository", c => new JobRepository(c.Get<SqliteConnection>("connection")));
        container.Set("person_repository", c => new PersonRepository(c.Get<SqliteConnection>("connection")));

        container.Set("job_notifier", c => new JobNotifierService(
            c.Get<IPersonRepository>("person_repository"),
            c.Get<IMailSender>("mail_sender"),
            c.Get<ISmsSender>("sms_sender")));

        container.Set("job_service", c => new JobService(c.Get<IJobRepository>("job_repository")));

        container.Set("job_controller", c => new JobController(
            c.Get<JobService>("job_service"),
            c.Get<JobNotifierService>("job_notifier")));

        container.Set("home_controller", _ => new HomeController());
    }

    private static SqliteConnection CreateConnection()
    {
        var dbPath = Environment.GetEnvironmentVariable("DB_PATH") ?? Path.Combine("data", "jobs.db");
        var connection = new SqliteConnection($"Data Source={dbPath}");
        connection.Open();
        return connection;
    }
}
