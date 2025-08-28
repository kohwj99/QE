package com.example.qe.queryengine.operator;

import java.util.Set;

public class OperatorFactory {

    private final OperatorRegistry registry;

    public OperatorFactory(OperatorRegistry registry) {
        this.registry = registry;
    }

    public record ResolvedOperator(Operator operator, Class<?> valueType) {}

    public ResolvedOperator resolveWithDynamicValueType(String operatorName, Class<?> fieldType) {
        Set<Class<?>> supportedValueTypes = registry.getValueTypesForOperatorName(operatorName);

        if (supportedValueTypes.isEmpty()) {
            throw new IllegalArgumentException("No supported value types for operator: " + operatorName);
        }

        // Pick fieldType if possible, else fallback
        Class<?> valueType = supportedValueTypes.contains(fieldType)
                ? fieldType
                : supportedValueTypes.iterator().next();

        Operator operator = registry.getOperator(operatorName, fieldType);
        if (operator == null) {
            throw new IllegalArgumentException("No operator found for " + operatorName + " with field type " + fieldType.getSimpleName());
        }

        return new ResolvedOperator(operator, valueType);
    }
}
