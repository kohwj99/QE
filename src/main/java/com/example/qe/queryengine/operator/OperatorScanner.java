// src/main/java/com/example/qe/queryengine/operator/OperatorScanner.java
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

    @SuppressWarnings("unchecked")
    public void scanAndRegister(String basePackage) {
        logger.info("Starting operator scanning in package: {}", basePackage);
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> operatorClasses = reflections.getTypesAnnotatedWith(OperatorAnnotation.class);
        logger.info("Found {} classes annotated with @OperatorAnnotation", operatorClasses.size());

        for (Class<?> clazz : operatorClasses) {
            try {
                logger.debug("Processing operator class: {}", clazz.getName());
                OperatorAnnotation annotation = clazz.getAnnotation(OperatorAnnotation.class);
                GenericOperator<?> operatorInstance = (GenericOperator<?>) clazz.getDeclaredConstructor().newInstance();

                String operatorName = annotation.value();
                Class<?>[] fieldTypes = annotation.supportedFieldTypes();
                Class<?>[] valueTypes = annotation.supportedValueTypes();

                for (Class<?> fieldType : fieldTypes) {
                    for (Class<?> valueType : valueTypes) {
                        registry.register(operatorName, fieldType, (Class<Object>) valueType, (GenericOperator<Object>) operatorInstance);
                        logger.debug("Registered operator '{}' for field type '{}' and value type '{}'",
                                operatorName, fieldType.getSimpleName(), valueType.getSimpleName());
                    }
                }

                logger.info("Successfully registered operator '{}' for {} field types and {} value types",
                        operatorName, fieldTypes.length, valueTypes.length);
            } catch (Exception e) {
                logger.error("Failed to instantiate operator {}: {}", clazz.getName(), e.getMessage(), e);
                throw new RuntimeException("Failed to instantiate operator " + clazz.getName(), e);
            }
        }

        logger.info("Completed operator scanning. Total operators processed: {}", operatorClasses.size());
    }
}