using Avature.JobNotifier.Domain;

namespace Avature.JobNotifier.Dao;

public interface IJobRepository
{
    int CreateJob(IJob job);
}
