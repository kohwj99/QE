package com.example.qe.util;
import com.example.qe.model.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OperatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

    // Map: operatorName -> (valueType -> operator instance)
    private final Map<String, Map<Class<?>, Operator<?, ?>>> operators = new HashMap<>();

    public void register(String operatorName, Class<?> valueType, Operator<?, ?> operator) {
        operators.computeIfAbsent(operatorName, k -> new HashMap<>())
                .put(valueType, operator);
        logger.debug("Registered operator '{}' for type '{}' using implementation '{}'",
                    operatorName, valueType.getSimpleName(), operator.getClass().getSimpleName());
    }

    public Operator<?, ?> get(String operatorName, Class<?> valueType) {
        Map<Class<?>, Operator<?, ?>> byType = operators.get(operatorName);
        if (byType == null) {
            logger.warn("No operator found for name: '{}'", operatorName);
            return null;
        }

        Operator<?, ?> operator = byType.get(valueType);
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

    public Map<Class<?>, Operator<?, ?>> getOperatorsForName(String operatorName) {
        return operators.get(operatorName);
    }

    public int getTotalOperatorCount() {
        return operators.values().stream()
                .mapToInt(Map::size)
                .sum();
    }
}
