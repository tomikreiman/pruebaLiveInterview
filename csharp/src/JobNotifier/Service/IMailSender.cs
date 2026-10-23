namespace Avature.JobNotifier.Service;

public interface IMailSender
{
    /// <exception cref="MailCannotBeSentException">The email cannot be sent.</exception>
    void Send(string mail, string message);
}
