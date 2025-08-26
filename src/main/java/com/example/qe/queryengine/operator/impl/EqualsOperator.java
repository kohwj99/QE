package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "equals",
        types = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        description = "Checks if a field equals the given value"
)
public class EqualsOperator<T> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.eq(value);
    }
}
