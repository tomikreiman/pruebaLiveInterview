namespace Avature.JobNotifier.Service;

public class MailCannotBeSentException : Exception
{
    public MailCannotBeSentException(string message) : base(message)
    {
    }

    public MailCannotBeSentException(string message, Exception innerException) : base(message, innerException)
    {
    }
}
