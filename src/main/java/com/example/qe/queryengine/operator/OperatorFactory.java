package com.example.qe.queryengine.operator;

import java.util.Arrays;

public class OperatorFactory {

    private final OperatorRegistry registry;

    public OperatorFactory(OperatorRegistry registry) {
        this.registry = registry;
    }

    /** Resolve a CustomOperator<F,V> by name and field type */
    public <F, V> CustomOperator<F, V> resolve(String operatorName, Class<F> fieldType) {
        CustomOperator<F, V> op = registry.get(operatorName, fieldType);
        if (op == null) {
            throw new IllegalArgumentException("No operator found for " + operatorName + " and field type " + fieldType.getName());
        }

        // Validate against OperatorAnnotation if present
        OperatorAnnotation annotation = op.getClass().getAnnotation(OperatorAnnotation.class);
        if (annotation != null && !Arrays.asList(annotation.types()).contains(fieldType)) {
            throw new IllegalArgumentException(
                    String.format("Operator '%s' does not support type '%s'. Supported types: %s",
                            operatorName, fieldType.getSimpleName(), Arrays.toString(annotation.types()))
            );
        }

        return op;
    }

    /** Resolve an optional RunConditionOperator */
    public RunConditionOperator resolveRunCondition(String operatorName) {
        RunConditionOperator op = registry.getRunCondition(operatorName);
        if (op == null) {
            throw new IllegalArgumentException("No RunConditionOperator found for " + operatorName);
        }
        return op;
    }
}
