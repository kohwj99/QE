package com.example.qe.queryengine.operator;
import com.example.qe.queryengine.exception.QueryEngineException;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;

@Slf4j
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
                GenericOperator operatorInstance = (GenericOperator) clazz.getDeclaredConstructor().newInstance();
                String operatorName = annotation.value();
                Class<?>[] fieldTypes = annotation.supportedFieldTypes();
                Class<?>[] valueTypes = annotation.supportedValueTypes();
                registry.register(operatorName, fieldTypes, valueTypes, operatorInstance);
            } catch (Exception e) {
                throw new QueryEngineException("Failed to instantiate operator " + clazz.getName(), e);
            }
        }
        log.debug("Completed operator scanning. Total operators processed: {}", operatorClasses.size());
    }
}
