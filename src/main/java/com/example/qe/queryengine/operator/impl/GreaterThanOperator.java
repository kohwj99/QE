package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "greaterThan",
        types = {BigDecimal.class, LocalDate.class},
        description = "Checks if a field is greater than the given value"
)
public class GreaterThanOperator<T extends Comparable<T>> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.gt(value);
    }
}
