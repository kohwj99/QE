package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.exception.QueryReplaceableException;
import org.springframework.stereotype.Component;

@Component
public class ReplaceableFactory {

    private final ReplaceableRegistry registry;

    public ReplaceableFactory(ReplaceableRegistry registry) {
        this.registry = registry;
    }

    public Replaceable create(String placeholder) {
        Class<? extends Replaceable> clazz = registry.get(placeholder);
        if (clazz == null) {
            throw new QueryReplaceableException("No Replaceable found for placeholder: " + placeholder);
        }

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new QueryReplaceableException("Failed to create Replaceable for " + placeholder, e);
        }
    }
}
