using System.Text.Json;
using Avature.JobNotifier.Domain;
using Microsoft.Data.Sqlite;

namespace Avature.JobNotifier.Dao;

public class JobRepository : IJobRepository
{
    private readonly SqliteConnection _connection;

    public JobRepository(SqliteConnection connection)
    {
        _connection = connection;
    }

    public int CreateJob(IJob job)
    {
        using var command = _connection.CreateCommand();
        command.CommandText = "INSERT INTO jobs (name, tags, salary) VALUES ($name, $tags, $salary)";
        command.Parameters.AddWithValue("$name", job.GetName()!);
        command.Parameters.AddWithValue("$tags", JsonSerializer.Serialize(job.GetTags()));
        command.Parameters.AddWithValue("$salary", job.GetSalary());
        command.ExecuteNonQuery();

        using var idCommand = _connection.CreateCommand();
        idCommand.CommandText = "SELECT last_insert_rowid()";
        return Convert.ToInt32(idCommand.ExecuteScalar());
    }
}
