using Avature.JobNotifier.Dao;
using Avature.JobNotifier.Domain;

namespace Avature.JobNotifier.Service;

public class JobNotifier
{
    private readonly IPersonRepository _personRepository;
    private readonly IMailSender _mailSender;
    private readonly ISmsSender _smsSender;

    public JobNotifier(IPersonRepository personRepository, IMailSender mailSender, ISmsSender smsSender)
    {
        _personRepository = personRepository;
        _mailSender = mailSender;
        _smsSender = smsSender;
    }

    public void Notify(IJob job)
    {
        foreach (IPerson person in _personRepository.GetAll())
        {
            if (!job.GetTags().Contains(person.GetPersonalTag()))
            {
                continue;
            }

            NotifyPerson(person, job);
        }
    }

    private void NotifyPerson(IPerson person, IJob job)
    {
        if (person.GetPhone() is not null)
        {
            _smsSender.Send(person.GetPhone()!, GetMessage(job));
        }

        if (person.GetMail() is not null)
        {
            _mailSender.Send(person.GetMail()!, GetMessage(job));
        }
    }

    private string GetMessage(IJob job)
    {
        string salary;
        if (job.GetSalary() > 100000)
        {
            salary = 100000 + "+";
        }
        else if (job.GetSalary() < 30000)
        {
            salary = 30000 + "-";
        }
        else
        {
            salary = job.GetSalary().ToString();
        }

        return "There is a new alert for your subscription. Job "
            + job.GetName()
            + ", with salary "
            + salary
            + ".";
    }
}
