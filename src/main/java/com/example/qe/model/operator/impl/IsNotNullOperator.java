package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "isNotNull",
        types = {Object.class},  // supports any type
        description = "Checks if a field is not null"
)
public class IsNotNullOperator<T> implements GenericOperator<T> {

    @Override
    public Condition apply(Field<T> field, T value) {
        // value is ignored because not-null check doesn’t need a value
        return field.isNotNull();
    }
}
