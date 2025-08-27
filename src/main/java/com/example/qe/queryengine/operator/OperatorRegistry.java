package com.example.qe.queryengine.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OperatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(OperatorRegistry.class);

    private final Map<String, Map<Class<?>, CustomOperator<?, ?>>> operators = new HashMap<>();
    private final Map<String, RunConditionOperator> runConditionOperators = new HashMap<>();

    public <F, V> void register(String operatorName, Class<F> fieldType, CustomOperator<F, V> operator) {
        operators.computeIfAbsent(operatorName, k -> new HashMap<>())
                .put(fieldType, operator);
        logger.debug("Registered CustomOperator '{}' for field type '{}'", operatorName, fieldType.getSimpleName());

        if (operator instanceof RunConditionOperator runOp) {
            runConditionOperators.put(operatorName, runOp);
            logger.debug("Registered RunConditionOperator '{}' for '{}'", operatorName, runOp.getClass().getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    public <F, V> CustomOperator<F, V> get(String operatorName, Class<F> fieldType) {
        Map<Class<?>, CustomOperator<?, ?>> byType = operators.get(operatorName);
        if (byType == null) return null;
        return (CustomOperator<F, V>) byType.get(fieldType);
    }

    public RunConditionOperator getRunCondition(String operatorName) {
        return runConditionOperators.get(operatorName);
    }

    public Set<String> getAllOperatorNames() {
        return operators.keySet();
    }
}
