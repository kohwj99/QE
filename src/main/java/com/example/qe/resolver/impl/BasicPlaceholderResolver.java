package com.example.qe.resolver.impl;

import com.example.qe.resolver.ValueResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Single resolver that handles basic hardcoded placeholders for StringQuery and DateQuery
 */
@Component
public class BasicPlaceholderResolver implements ValueResolver {

    @Override
    public boolean canResolve(String placeholder) {
        return switch (placeholder.toLowerCase()) {
            case "me", "today" -> true;
            default -> false;
        };
    }

    @Override
    public Object resolve(String placeholder, Class<?> targetType) {
        return switch (placeholder.toLowerCase()) {
            case "me" -> "current_user";
            case "today" -> LocalDate.now().toString(); // Always return as string for JSON compatibility
            default -> throw new IllegalArgumentException("Unsupported placeholder: " + placeholder);
        };
    }
}
