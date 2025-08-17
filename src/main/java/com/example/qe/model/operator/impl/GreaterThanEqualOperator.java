package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "greaterThanEqual",
        types = {Integer.class, java.time.LocalDate.class},
        description = "Checks if a field is greater than or equal to the given value"
)
public class GreaterThanEqualOperator<T extends Comparable<T>> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.ge(value);
    }
}
