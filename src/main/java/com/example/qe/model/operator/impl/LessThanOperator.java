package com.example.qe.model.operator.impl;


import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "lessThan",
        types = {Integer.class, Double.class, java.time.LocalDate.class},
        description = "Checks if a field is less than the given value"
)
public class LessThanOperator<T extends Comparable<T>> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.lt(value);
    }
}
