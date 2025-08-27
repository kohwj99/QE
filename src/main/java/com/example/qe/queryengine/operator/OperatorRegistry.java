// src/main/java/com/example/qe/queryengine/operator/OperatorRegistry.java
package com.example.qe.queryengine.operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class OperatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

    private final Map<Class<?>, Set<String>> fieldTypeToOperatorNames = new HashMap<>();
    private final Map<String, Set<Class<?>>> operatorNameToValueTypes = new HashMap<>();
    private final Map<String, Map<Class<?>, GenericOperator<?>>> operatorInstances = new HashMap<>();

    public <T> void register(String operatorName, Class<?> fieldType, Class<T> valueType, GenericOperator<T> operator) {
        fieldTypeToOperatorNames.computeIfAbsent(fieldType, k -> new HashSet<>()).add(operatorName);
        operatorNameToValueTypes.computeIfAbsent(operatorName, k -> new HashSet<>()).add(valueType);
        operatorInstances.computeIfAbsent(operatorName, k -> new HashMap<>()).put(valueType, operator);
        logger.debug("Registered operator '{}' for field type '{}' and value type '{}'", operatorName, fieldType.getSimpleName(), valueType.getSimpleName());
    }

    public Set<String> getOperatorNamesForFieldType(Class<?> fieldType) {
        return fieldTypeToOperatorNames.getOrDefault(fieldType, Collections.emptySet());
    }

    public Set<Class<?>> getValueTypesForOperatorName(String operatorName) {
        return operatorNameToValueTypes.getOrDefault(operatorName, Collections.emptySet());
    }

    @SuppressWarnings("unchecked")
    public <T> GenericOperator<T> getOperator(String operatorName, Class<?> fieldType, Class<T> valueType) {
        Set<String> validOperators = fieldTypeToOperatorNames.getOrDefault(fieldType, Collections.emptySet());
        if (!validOperators.contains(operatorName)) {
            logger.warn("Operator '{}' is not supported for field type '{}'", operatorName, fieldType.getSimpleName());
            return null;
        }
        Map<Class<?>, GenericOperator<?>> byType = operatorInstances.get(operatorName);
        if (byType == null) {
            logger.warn("No operator found for name: '{}'", operatorName);
            return null;
        }
        GenericOperator<T> operator = (GenericOperator<T>) byType.get(valueType);
        if (operator == null) {
            logger.warn("No operator '{}' found for value type: '{}'", operatorName, valueType.getSimpleName());
        } else {
            logger.debug("Retrieved operator '{}' for field type '{}' and value type '{}': {}",
                    operatorName, fieldType.getSimpleName(), valueType.getSimpleName(), operator.getClass().getSimpleName());
        }
        return operator;
    }

    public Set<Class<?>> getSupportedValueTypes(String operatorName, Class<?> fieldType) {
        Map<Class<?>, Set<Class<?>>> fieldTypeMap = operatorNameToValueTypes.get(operatorName);
        if (fieldTypeMap == null) {
            return Collections.emptySet();
        }
        Set<Class<?>> valueTypes = fieldTypeMap.get(fieldType);
        if (valueTypes == null) {
            return Collections.emptySet();
        }
        return valueTypes;

    }
}