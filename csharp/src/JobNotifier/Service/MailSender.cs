using Microsoft.Extensions.Logging;

namespace Avature.JobNotifier.Service;

public class MailSender : IMailSender
{
    private readonly ILogger _logger;

    public MailSender(ILogger logger)
    {
        _logger = logger;
    }

    public void Send(string mail, string message)
    {
        _logger.LogInformation("Sending email to: {Mail} [message={MessageText}]", mail, message);
    }
}
