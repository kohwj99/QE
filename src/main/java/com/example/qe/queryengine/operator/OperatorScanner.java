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
        logger.info("Scanning for operators in package: {}", basePackage);
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> operatorClasses = reflections.getTypesAnnotatedWith(OperatorAnnotation.class);
        logger.info("Found {} operator classes", operatorClasses.size());

        for (Class<?> clazz : operatorClasses) {
            try {
                logger.debug("Processing {}", clazz.getName());

                OperatorAnnotation annotation = clazz.getAnnotation(OperatorAnnotation.class);
                Operator operatorInstance = (Operator) clazz.getDeclaredConstructor().newInstance();

                String operatorName = annotation.value();
                Class<?>[] fieldTypes = annotation.supportedFieldTypes();
                Class<?>[] valueTypes = annotation.supportedValueTypes();

                for (Class<?> fieldType : fieldTypes) {
                    for (Class<?> valueType : valueTypes) {
                        registry.register(operatorName, fieldType, valueType, operatorInstance);
                        logger.debug("Registered '{}' for field '{}' and value '{}'",
                                operatorName, fieldType.getSimpleName(), valueType.getSimpleName());
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to instantiate operator {}: {}", clazz.getName(), e.getMessage(), e);
                throw new RuntimeException("Failed to instantiate operator " + clazz.getName(), e);
            }
        }
    }
}
