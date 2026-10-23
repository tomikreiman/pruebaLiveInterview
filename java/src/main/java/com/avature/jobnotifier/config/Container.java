package com.avature.jobnotifier.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Container {
    private final Map<String, Function<Container, Object>> registrations = new HashMap<>();
    private final Map<String, Object> singletons = new HashMap<>();

    public void set(String key, Function<Container, Object> factory) {
        registrations.put(key, factory);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (!registrations.containsKey(key)) {
            throw new IllegalStateException("No registration for " + key);
        }
        if (!singletons.containsKey(key)) {
            singletons.put(key, registrations.get(key).apply(this));
        }
        return (T) singletons.get(key);
    }
}
