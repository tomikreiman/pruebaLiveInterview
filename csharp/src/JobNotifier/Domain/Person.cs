namespace Avature.JobNotifier.Domain;

public class Person : IPerson
{
    private readonly string _name;
    private readonly string _personalTag;
    private readonly string? _mail;
    private readonly string? _phone;

    public Person(string name, string personalTag, string? mail, string? phone)
    {
        _name = name;
        _personalTag = personalTag;
        _mail = mail;
        _phone = phone;
    }

    public string GetName()
    {
        return _name;
    }

    public string GetPersonalTag()
    {
        return _personalTag;
    }

    public string? GetPhone()
    {
        return _phone;
    }

    public string? GetMail()
    {
        return _mail;
    }
}
