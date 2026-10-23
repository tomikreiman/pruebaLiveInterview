using Avature.JobNotifier.Domain;
using Microsoft.Data.Sqlite;

namespace Avature.JobNotifier.Dao;

public class PersonRepository : IPersonRepository
{
    private readonly SqliteConnection _connection;

    public PersonRepository(SqliteConnection connection)
    {
        _connection = connection;
    }

    public List<Person> GetAll()
    {
        using var command = _connection.CreateCommand();
        command.CommandText = "SELECT id, name, personal_tag, mail, phone FROM people";

        using var rows = command.ExecuteReader();
        var people = new List<Person>();
        while (rows.Read())
        {
            people.Add(new Person(
                rows.GetString(rows.GetOrdinal("name")),
                rows.GetString(rows.GetOrdinal("personal_tag")),
                rows.IsDBNull(rows.GetOrdinal("mail")) ? null : rows.GetString(rows.GetOrdinal("mail")),
                rows.IsDBNull(rows.GetOrdinal("phone")) ? null : rows.GetString(rows.GetOrdinal("phone"))));
        }

        return people;
    }
}
