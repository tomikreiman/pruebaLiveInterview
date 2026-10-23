namespace Avature.JobNotifier.Service;

public interface ISmsSender
{
    /// <exception cref="SmsCannotBeSentException">The SMS cannot be sent.</exception>
    void Send(string phone, string message);
}
