using Microsoft.Data.Sqlite;

namespace Avature.JobNotifier.Scripts;

public static class InitDb
{
    public static void Run(string dbPath)
    {
        var directory = Path.GetDirectoryName(dbPath);
        if (directory is not null)
        {
            Directory.CreateDirectory(directory);
        }

        try
        {
            using var connection = new SqliteConnection($"Data Source={dbPath}");
            connection.Open();

            using (var command = connection.CreateCommand())
            {
                command.CommandText = """
                    CREATE TABLE IF NOT EXISTS jobs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        tags TEXT,
                        salary INTEGER,
                        start_hour INTEGER,
                        end_hour INTEGER,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                    );

                    CREATE TABLE IF NOT EXISTS people (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        personal_tag TEXT NOT NULL,
                        mail TEXT,
                        phone TEXT,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                    );
                    """;
                command.ExecuteNonQuery();
            }

            SeedJobsIfEmpty(connection);
            SeedPeopleIfEmpty(connection);

            Console.WriteLine("Database initialized successfully.");
        }
        catch (SqliteException exception)
        {
            Console.Error.WriteLine("Database connection failed: " + exception.Message);
            throw;
        }
    }

    private static void SeedJobsIfEmpty(SqliteConnection connection)
    {
        if (Count(connection, "jobs") != 0)
        {
            return;
        }

        object[][] samples =
        [
            ["Alpha", "[\"php\"]", 50000, 9, 10],
            ["Bravo", "[\"go\"]", 60000, 10, 11],
            ["Charlie", "[\"php\",\"backend\"]", 70000, 11, 12],
            ["Delta", "[\"remote\"]", 80000, 13, 14],
            ["Echo", "[\"php\"]", 90000, 8, 17]
        ];

        foreach (var sample in samples)
        {
            using var command = connection.CreateCommand();
            command.CommandText = """
                INSERT INTO jobs (name, tags, salary, start_hour, end_hour)
                VALUES ($name, $tags, $salary, $startHour, $endHour)
                """;
            command.Parameters.AddWithValue("$name", sample[0]);
            command.Parameters.AddWithValue("$tags", sample[1]);
            command.Parameters.AddWithValue("$salary", sample[2]);
            command.Parameters.AddWithValue("$startHour", sample[3]);
            command.Parameters.AddWithValue("$endHour", sample[4]);
            command.ExecuteNonQuery();
        }
    }

    private static void SeedPeopleIfEmpty(SqliteConnection connection)
    {
        if (Count(connection, "people") != 0)
        {
            return;
        }

        string[][] samples =
        [
            ["Alice", "php", "alice@example.com", "123-456-7890"],
            ["Bob", "go", "bob@example.com", "234-567-8901"],
            ["Charlie", "remote", "charlie@example.com", "345-678-9012"]
        ];

        foreach (var sample in samples)
        {
            using var command = connection.CreateCommand();
            command.CommandText = """
                INSERT INTO people (name, personal_tag, mail, phone)
                VALUES ($name, $personalTag, $mail, $phone)
                """;
            command.Parameters.AddWithValue("$name", sample[0]);
            command.Parameters.AddWithValue("$personalTag", sample[1]);
            command.Parameters.AddWithValue("$mail", sample[2]);
            command.Parameters.AddWithValue("$phone", sample[3]);
            command.ExecuteNonQuery();
        }
    }

    private static int Count(SqliteConnection connection, string table)
    {
        using var command = connection.CreateCommand();
        command.CommandText = $"SELECT COUNT(*) FROM {table}";
        return Convert.ToInt32(command.ExecuteScalar());
    }
}
