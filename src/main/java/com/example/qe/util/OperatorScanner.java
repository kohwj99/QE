package com.example.qe.util;
import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.reflections.Reflections;

import java.util.Set;

public class OperatorScanner {

    private final OperatorRegistry registry;

    public OperatorScanner(OperatorRegistry registry) {
        this.registry = registry;
    }

    public void scanAndRegister(String basePackage) {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> operatorClasses = reflections.getTypesAnnotatedWith(OperatorAnnotation.class);

        for (Class<?> clazz : operatorClasses) {
            try {
                OperatorAnnotation annotation = clazz.getAnnotation(OperatorAnnotation.class);
                GenericOperator<?> operatorInstance = (GenericOperator<?>) clazz.getDeclaredConstructor().newInstance();

                for (Class<?> type : annotation.types()) {
                    registry.register(annotation.value(), type, (GenericOperator) operatorInstance);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate operator " + clazz.getName(), e);
            }
        }
    }
}

