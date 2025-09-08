package com.example.qe.queryengine.operator;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OperatorRegistry {
    private final Map<String, Set<Class<?>>> operatorToFieldTypes = new HashMap<>();
    private final Map<String, Set<Class<?>>> operatorToValueTypes = new HashMap<>();
    private final Map<String, GenericOperator> operatorMap = new HashMap<>();

    public void register(String name, Class<?>[] fieldTypes, Class<?>[] valueTypes, GenericOperator operator) {
        operatorToFieldTypes.computeIfAbsent(name, k -> new HashSet<>()).addAll(Arrays.asList(fieldTypes));
        operatorToValueTypes.computeIfAbsent(name, k -> new HashSet<>()).addAll(Arrays.asList(valueTypes));
        operatorMap.put(name, operator);
    }

    public GenericOperator get(String name, Class<?> fieldType, Class<?> valueType) {
        Set<Class<?>> fieldTypes = operatorToFieldTypes.get(name);
        Set<Class<?>> valueTypes = operatorToValueTypes.get(name);
        if (fieldTypes == null || valueTypes == null) return null;
        if (!fieldTypes.contains(fieldType) || !valueTypes.contains(valueType)) return null;
        return operatorMap.get(name);
    }

    public Set<Class<?>> getSupportedValueTypes(String operatorName) {
        return operatorToValueTypes.getOrDefault(operatorName, Collections.emptySet());
    }

    public Object getOperator(String name) {
        return operatorMap.get(name);
    }
}
