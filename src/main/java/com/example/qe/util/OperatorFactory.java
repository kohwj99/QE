package com.example.qe.util;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.Operator;

import java.util.Arrays;

public class OperatorFactory {

    private final OperatorRegistry registry;

    public OperatorFactory(OperatorRegistry registry) {
        this.registry = registry;
    }

    /**
     * Resolves the operator by name and value type.
     * Throws an exception if no matching operator is found.
     */
    public Operator<?, ?> resolve(String operatorName, Class<?> valueType) {
        Operator<?, ?> operator = registry.get(operatorName, valueType);
        if (operator == null) {
            throw new IllegalArgumentException(
                    "No operator found for name: " + operatorName + " and type: " + valueType.getName());
        }
        Class<?> operatorClass = operator.getClass();
        OperatorAnnotation annotation = operatorClass.getAnnotation(OperatorAnnotation.class);

        if (annotation != null && annotation.types().length > 0) {
            boolean typeSupported = Arrays.asList(annotation.types()).contains(valueType);
            if (!typeSupported) {
                throw new IllegalArgumentException(
                        String.format("Operator '%s' does not support type '%s'. Supported types: %s",
                                operatorName, valueType.getSimpleName(),
                                Arrays.toString(annotation.types())));
            }
        }

        return operator;
    }
}
