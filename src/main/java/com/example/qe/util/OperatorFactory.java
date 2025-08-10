package com.example.qe.util;

import com.example.qe.model.operator.GenericOperator;

public class OperatorFactory {

    private final OperatorRegistry registry;

    public OperatorFactory(OperatorRegistry registry) {
        this.registry = registry;
    }

    /**
     * Resolves the operator by name and value type.
     * Throws an exception if no matching operator is found.
     */
    public <T> GenericOperator<T> resolve(String operatorName, Class<T> valueType) {
        GenericOperator<T> operator = registry.get(operatorName, valueType);
        if (operator == null) {
            throw new IllegalArgumentException(
                    "No operator found for name: " + operatorName + " and type: " + valueType.getName());
        }
        return operator;
    }
}

