package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "notEquals",
        types = {String.class, Integer.class, Boolean.class, java.time.LocalDate.class},
        description = "Checks if a field does not equal the given value"
)
public class NotEqualsOperator<T> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.ne(value);
    }
}
