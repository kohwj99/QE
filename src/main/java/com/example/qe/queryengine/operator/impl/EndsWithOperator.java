package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "endsWith",
        supportedFieldTypes = {String.class},
        supportedValueTypes = {String.class},
        description = "Checks if a string field ends with a given pattern"
)
public class EndsWithOperator implements GenericOperator {
    @Override
    public Condition apply(Field<?> field, Object value) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("EndsWithOperator expects a String value");
        }
        return field.like("%" + value);
    }
}
