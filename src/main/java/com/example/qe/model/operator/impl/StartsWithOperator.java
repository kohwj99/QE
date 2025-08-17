package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "startsWith",
        types = {String.class},
        description = "Checks if a string field starts with the given value"
)
public class StartsWithOperator implements GenericOperator<String> {
    @Override
    public Condition apply(Field<String> field, String value) {
        // Use LIKE with pattern 'value%' to check if field starts with the value
        return field.like(value + "%");
    }
}
