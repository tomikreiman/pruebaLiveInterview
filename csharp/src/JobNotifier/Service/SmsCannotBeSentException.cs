namespace Avature.JobNotifier.Service;

public class SmsCannotBeSentException : Exception
{
    public SmsCannotBeSentException(string message) : base(message)
    {
    }

    public SmsCannotBeSentException(string message, Exception innerException) : base(message, innerException)
    {
    }
}
