namespace Avature.JobNotifier.Domain;

public interface IJob
{
    string? GetName();
    List<string> GetTags();
    int GetSalary();
}
