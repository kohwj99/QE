package com.example.qe.model.operator.impl;


import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "lessThan",
        types = {BigDecimal.class, LocalDate.class},
        description = "Checks if a field is less than the given value"
)
public class LessThanOperator<T extends Comparable<T>> implements GenericOperator<T> {
    @Override
    public Condition apply(Field<T> field, T value) {
        return field.lt(value);
    }
}
