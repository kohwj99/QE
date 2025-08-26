package com.example.qe.queryengine.replacement;

public interface ReplacementResolver {

    boolean canResolve(String placeholder);

    Object resolve(String placeholder, Class<?> targetType);
}
