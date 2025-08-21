package com.example.qe.resolver;

/**
 * Interface for resolving dynamic placeholder values at runtime
 * Simplified - no priority needed since placeholders are hardcoded by developers with no overlaps
 */
public interface ValueResolver {

    /**
     * Checks if this resolver can handle the given placeholder
     */
    boolean canResolve(String placeholder);

    /**
     * Resolves the placeholder to an actual value
     * @param placeholder The placeholder name (without brackets)
     * @param targetType The expected return type
     * @return The resolved value
     */
    Object resolve(String placeholder, Class<?> targetType);
}
