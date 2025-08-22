package com.example.qe.resolver;

public interface ValueResolver {

    boolean canResolve(String placeholder);

    Object resolve(String placeholder, Class<?> targetType);
}
