package com.example.qe.queryengine.operator;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class OperatorScanner {

    private static final Logger logger = LoggerFactory.getLogger(OperatorScanner.class);
    private final OperatorRegistry registry;

    public OperatorScanner(OperatorRegistry registry) {
        this.registry = registry;
    }

    public void scanAndRegister(String basePackage) {
        logger.info("Scanning package: {}", basePackage);
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> operatorClasses = reflections.getTypesAnnotatedWith(OperatorAnnotation.class);
        logger.info("Found {} operator classes", operatorClasses.size());

        for (Class<?> clazz : operatorClasses) {
            try {
                OperatorAnnotation annotation = clazz.getAnnotation(OperatorAnnotation.class);
                Object instance = clazz.getDeclaredConstructor().newInstance();

                // Register as CustomOperator
                if (instance instanceof CustomOperator<?, ?> customOp) {
                    for (Class<?> fieldType : annotation.types()) {
                        // Suppress unchecked cast warning
                        @SuppressWarnings("unchecked")
                        Class<Object> castFieldType = (Class<Object>) fieldType;

                        @SuppressWarnings("unchecked")
                        CustomOperator<Object, Object> castOp = (CustomOperator<Object, Object>) customOp;

                        registry.register(annotation.value(), castFieldType, castOp);
                        logger.debug("Registered operator '{}' for type '{}'", annotation.value(), fieldType.getSimpleName());
                    }
                }

                // RunConditionOperator is automatically registered in registry.register()
                if (instance instanceof RunConditionOperator) {
                    logger.debug("Operator '{}' also implements RunConditionOperator", annotation.value());
                }

            } catch (Exception e) {
                logger.error("Failed to instantiate operator {}: {}", clazz.getName(), e.getMessage(), e);
                throw new RuntimeException("Failed to instantiate operator " + clazz.getName(), e);
            }
        }
    }
}
