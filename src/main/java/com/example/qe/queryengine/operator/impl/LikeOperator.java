package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "like",
        types = {String.class},
        description = "Checks if a string field matches the given pattern"
)
public class LikeOperator implements GenericOperator<String> {
    @Override
    public Condition apply(Field<String> field, String value) {
        return field.like(value);
    }
}
