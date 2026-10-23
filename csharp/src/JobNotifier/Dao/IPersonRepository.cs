using Avature.JobNotifier.Domain;

namespace Avature.JobNotifier.Dao;

public interface IPersonRepository
{
    List<Person> GetAll();
}
