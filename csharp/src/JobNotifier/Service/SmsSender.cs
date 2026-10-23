using Microsoft.Extensions.Logging;

namespace Avature.JobNotifier.Service;

public class SmsSender : ISmsSender
{
    private readonly ILogger _logger;

    public SmsSender(ILogger logger)
    {
        _logger = logger;
    }

    public void Send(string phone, string message)
    {
        _logger.LogInformation("Sending SMS to: {Phone} [message={MessageText}]", phone, message);
    }
}
