namespace Avature.JobNotifier.Domain;

public class Job : IJob
{
    private readonly string? _name;
    private readonly List<string> _tags;
    private readonly int _salary;

    public Job(string? name, List<string> tags, int salary)
    {
        _name = name;
        _tags = tags;
        _salary = salary;
    }

    public string? GetName()
    {
        return _name;
    }

    public List<string> GetTags()
    {
        return _tags;
    }

    public int GetSalary()
    {
        return _salary;
    }
}
