package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "startsWith",
        supportedFieldTypes = {String.class},
        supportedValueTypes = {String.class},
        description = "Checks if a string field starts with a given pattern"
)
public class StartsWithOperator implements GenericOperator{
    @Override
    public Condition apply(Field<?> field, Object value) {
        if (!(value instanceof String)) {
            throw new InvalidQueryException("StartsWithOperator expects a String value");
        }
        return field.like(value + "%");
    }
}
