package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.OperatorNotFoundException;

import java.util.Set;

public class OperatorFactory {

    private final OperatorRegistry registry;

    public OperatorFactory(OperatorRegistry registry) {
        this.registry = registry;
    }

    /**
     * Resolves the operator by name and value type.
     * Throws an exception if no matching operator is found.
     */

    public GenericOperator resolve(String operatorName, Class<?> fieldType, Class<?> valueType) {
        GenericOperator op = registry.get(operatorName, fieldType, valueType);
        if (op == null) {
            throw new OperatorNotFoundException("Operator " + operatorName +
                    " does not support field type " + fieldType.getName() +
                    " and value type " + valueType.getName());
        }
        return op;
    }

    public Class<?> resolveValueType(String operatorName, Class<?> fieldType) {
        Set<Class<?>> valueTypes = registry.getSupportedValueTypes(operatorName);
        if (valueTypes == null || valueTypes.isEmpty()) {
            throw new OperatorNotFoundException("No value types registered for operator: " + operatorName);
        }
        // If fieldType is supported as a value type, use it
        if (valueTypes.contains(fieldType)) {
            return fieldType;
        }
        // Otherwise, use the first available value type
        return valueTypes.iterator().next();
    }
}
