package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "greaterThan",
        types = {Integer.class, java.time.LocalDate.class},
        description = "Checks if a field is greater than the given value"
)
public class GreaterThanOperator<T extends Comparable<T>> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.gt(value);
    }
}
