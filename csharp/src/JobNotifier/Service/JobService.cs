using Avature.JobNotifier.Dao;
using Avature.JobNotifier.Domain;

namespace Avature.JobNotifier.Service;

public class JobService
{
    private readonly IJobRepository _jobRepository;

    public JobService(IJobRepository jobRepository)
    {
        _jobRepository = jobRepository;
    }

    public int CreateJob(IJob job)
    {
        if (string.IsNullOrEmpty(job.GetName()))
        {
            throw new ArgumentException("Name is required");
        }

        return _jobRepository.CreateJob(job);
    }
}
