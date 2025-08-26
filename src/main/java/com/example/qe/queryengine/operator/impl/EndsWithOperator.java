package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "endsWith",
        types = {String.class},
        description = "Checks if a string field ends with the given value"
)
public class EndsWithOperator implements GenericOperator<String> {
    @Override
    public Condition apply(Field<String> field, String value) {
        return field.like("%" + value);
    }
}
