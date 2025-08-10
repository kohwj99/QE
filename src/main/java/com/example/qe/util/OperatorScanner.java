package com.example.qe.util;
import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
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
        logger.info("Starting operator scanning in package: {}", basePackage);
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> operatorClasses = reflections.getTypesAnnotatedWith(OperatorAnnotation.class);
        logger.info("Found {} classes annotated with @OperatorAnnotation", operatorClasses.size());

        for (Class<?> clazz : operatorClasses) {
            try {
                logger.debug("Processing operator class: {}", clazz.getName());
                OperatorAnnotation annotation = clazz.getAnnotation(OperatorAnnotation.class);
                GenericOperator<?> operatorInstance = (GenericOperator<?>) clazz.getDeclaredConstructor().newInstance();

                logger.info("Registering operator '{}' from class: {}", annotation.value(), clazz.getSimpleName());

                for (Class<?> type : annotation.types()) {
                    registry.register(annotation.value(), type, (GenericOperator) operatorInstance);
                    logger.debug("  - Registered for type: {}", type.getSimpleName());
                }

                logger.info("Successfully registered operator '{}' for {} types",
                           annotation.value(), annotation.types().length);
            } catch (Exception e) {
                logger.error("Failed to instantiate operator {}: {}", clazz.getName(), e.getMessage(), e);
                throw new RuntimeException("Failed to instantiate operator " + clazz.getName(), e);
            }
        }

        logger.info("Completed operator scanning. Total operators processed: {}", operatorClasses.size());
    }
}
