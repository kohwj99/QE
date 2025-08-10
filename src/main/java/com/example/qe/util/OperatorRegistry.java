package com.example.qe.util;
import com.example.qe.model.operator.GenericOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OperatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

    // Map: operatorName -> (valueType -> operator instance)
    private final Map<String, Map<Class<?>, GenericOperator<?>>> operators = new HashMap<>();

    public <T> void register(String operatorName, Class<T> valueType, GenericOperator<T> operator) {
        operators.computeIfAbsent(operatorName, k -> new HashMap<>())
                .put(valueType, operator);
        logger.debug("Registered operator '{}' for type '{}' using implementation '{}'",
                    operatorName, valueType.getSimpleName(), operator.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public <T> GenericOperator<T> get(String operatorName, Class<T> valueType) {
        Map<Class<?>, GenericOperator<?>> byType = operators.get(operatorName);
        if (byType == null) {
            logger.warn("No operator found for name: '{}'", operatorName);
            return null;
        }

        GenericOperator<T> operator = (GenericOperator<T>) byType.get(valueType);
        if (operator == null) {
            logger.warn("No operator '{}' found for type: '{}'", operatorName, valueType.getSimpleName());
        } else {
            logger.debug("Retrieved operator '{}' for type '{}': {}",
                        operatorName, valueType.getSimpleName(), operator.getClass().getSimpleName());
        }
        return operator;
    }

    public Set<String> getAllOperatorNames() {
        return operators.keySet();
    }

    public Map<Class<?>, GenericOperator<?>> getOperatorsForName(String operatorName) {
        return operators.get(operatorName);
    }

    public int getTotalOperatorCount() {
        return operators.values().stream()
                .mapToInt(Map::size)
                .sum();
    }
}
