package com.example.qe.queryengine.replaceable;

@FunctionalInterface
public interface Replaceable {
    String resolve(String rawValue);
}
