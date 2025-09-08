package com.example.qe.queryengine.replaceable;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReplaceableRegistry {

    private final Map<String, Class<? extends Replaceable>> registry = new HashMap<>();

    public void register(String placeholder, Class<? extends Replaceable> clazz) {
        registry.put(placeholder, clazz);
    }

    public Class<? extends Replaceable> get(String placeholder) {
        return registry.get(placeholder);
    }

    public boolean contains(String placeholder) {
        return registry.containsKey(placeholder);
    }

    public Map<String, Class<? extends Replaceable>> getAll() {
        return registry;
    }
}
