namespace Avature.JobNotifier.Config;

public class Container
{
    private readonly Dictionary<string, Func<Container, object>> _registrations = new();
    private readonly Dictionary<string, object> _singletons = new();

    public void Set(string key, Func<Container, object> factory)
    {
        _registrations[key] = factory;
    }

    public T Get<T>(string key)
    {
        if (!_registrations.ContainsKey(key))
        {
            throw new InvalidOperationException($"No registration for {key}");
        }

        if (!_singletons.ContainsKey(key))
        {
            _singletons[key] = _registrations[key](this);
        }

        return (T)_singletons[key];
    }
}
