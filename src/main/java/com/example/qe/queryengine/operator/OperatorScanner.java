package com.example.qe.queryengine.operator;
import com.example.qe.queryengine.exception.QueryEngineException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class OperatorScanner {

    private static final String OPERATOR_BASE_PACKAGE = "com.example.qe.queryengine.operator.impl";

    private final OperatorRegistry registry;

    public OperatorScanner(OperatorRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void scanAndRegister() {
        Reflections reflections = new Reflections(OPERATOR_BASE_PACKAGE);
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
