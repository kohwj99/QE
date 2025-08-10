package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "equals",
        types = {String.class, Integer.class, Boolean.class, java.time.LocalDate.class},
        description = "Checks if a field equals the given value"
)
public class EqualsOperator<T> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.eq(value);
    }
}

