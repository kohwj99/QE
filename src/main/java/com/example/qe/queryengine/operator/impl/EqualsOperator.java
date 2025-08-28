package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "equals",
        supportedFieldTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        supportedValueTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        description = "Checks if a field equals the given value"
)
public class EqualsOperator implements GenericOperator {

    @SuppressWarnings("unchecked")
    @Override
    public Condition apply(Field<?> field, Object value) {
        return ((Field<Object>) field).eq(value);
    }
}
