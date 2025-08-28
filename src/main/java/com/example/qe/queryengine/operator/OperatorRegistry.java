package com.example.qe.queryengine.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OperatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

    // fieldType -> operator names
    private final Map<Class<?>, Set<String>> fieldTypeToOperatorNames = new HashMap<>();

    // operator name -> supported value types
    private final Map<String, Set<Class<?>>> operatorNameToValueTypes = new HashMap<>();

    // operator name -> instance
    private final Map<String, Operator> operatorInstances = new HashMap<>();

    public void register(String operatorName, Class<?> fieldType, Class<?> valueType, Operator operator) {
        fieldTypeToOperatorNames.computeIfAbsent(fieldType, k -> new HashSet<>()).add(operatorName);
        operatorNameToValueTypes.computeIfAbsent(operatorName, k -> new HashSet<>()).add(valueType);
        operatorInstances.put(operatorName, operator);

        logger.debug("Registered operator '{}' for field '{}' with value '{}'",
                operatorName, fieldType.getSimpleName(), valueType.getSimpleName());
    }

    public Set<String> getOperatorNamesForFieldType(Class<?> fieldType) {
        return fieldTypeToOperatorNames.getOrDefault(fieldType, Collections.emptySet());
    }

    public Set<Class<?>> getValueTypesForOperatorName(String operatorName) {
        return operatorNameToValueTypes.getOrDefault(operatorName, Collections.emptySet());
    }

    public Operator getOperator(String operatorName, Class<?> fieldType) {
        if (!fieldTypeToOperatorNames.getOrDefault(fieldType, Collections.emptySet()).contains(operatorName)) {
            logger.warn("Operator '{}' not supported for field '{}'", operatorName, fieldType.getSimpleName());
            return null;
        }
        return operatorInstances.get(operatorName);
    }
}
