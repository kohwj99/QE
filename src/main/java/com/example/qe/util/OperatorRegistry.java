package com.example.qe.util;
import com.example.qe.model.operator.GenericOperator;

import java.util.*;

public class OperatorRegistry {

    // Map: operatorName -> (valueType -> operator instance)
    private final Map<String, Map<Class<?>, GenericOperator<?>>> operators = new HashMap<>();

    public <T> void register(String operatorName, Class<T> valueType, GenericOperator<T> operator) {
        operators.computeIfAbsent(operatorName, k -> new HashMap<>())
                .put(valueType, operator);
    }

    @SuppressWarnings("unchecked")
    public <T> GenericOperator<T> get(String operatorName, Class<T> valueType) {
        Map<Class<?>, GenericOperator<?>> byType = operators.get(operatorName);
        if (byType == null) return null;
        return (GenericOperator<T>) byType.get(valueType);
    }
}
