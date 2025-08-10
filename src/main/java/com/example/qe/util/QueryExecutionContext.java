package com.example.qe.util;
import com.example.qe.model.operator.GenericOperator;
import org.springframework.core.convert.ConversionService;

public class QueryExecutionContext {

    private final OperatorFactory operatorFactory;
    private final ConversionService conversionService;

    public QueryExecutionContext(OperatorFactory operatorFactory, ConversionService conversionService) {
        this.operatorFactory = operatorFactory;
        this.conversionService = conversionService;
    }

    public <T> GenericOperator<T> resolveOperator(String operatorName, Class<T> valueType) {
        return operatorFactory.resolve(operatorName, valueType);
    }

    public <T> T convert(Object source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }
}