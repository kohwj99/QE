package com.example.qe.queryengine.operator;

public class OperatorFactory {

    private final OperatorRegistry registry;

    public OperatorFactory(OperatorRegistry registry) {
        this.registry = registry;
    }

    public record ResolvedOperator(GenericOperator<?> operator, Class<?> valueType) {
    }

    public ResolvedOperator resolveWithDynamicValueType(String operatorName, Class<?> fieldType) {
        var supportedValueTypes = registry.getSupportedValueTypes(operatorName, fieldType);
        Class<?> valueType = supportedValueTypes.contains(fieldType)
                ? fieldType
                : supportedValueTypes.iterator().next();
        GenericOperator<?> operator = registry.getOperator(operatorName, fieldType, valueType);
        if (operator == null) {
            throw new IllegalArgumentException("No operator found for " + operatorName);
        }
        return new ResolvedOperator(operator, valueType);
    }

//    /**
//     * Resolves the operator by name, field type, and value type.
//     * Throws an exception if no matching operator is found or types are not supported.
//     */
//    public <T> GenericOperator<T> resolve(String operatorName, Class<?> fieldType, Class<T> valueType) {
//        GenericOperator<T> operator = (GenericOperator<T>) registry.getOperator(operatorName, fieldType, valueType);
//        if (operator == null) {
//            throw new IllegalArgumentException(
//                    String.format("No operator found for name: %s, field type: %s, value type: %s",
//                            operatorName, fieldType.getSimpleName(), valueType.getSimpleName())
//            );
//        }
//        return operator;
//    }
}

