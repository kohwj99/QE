package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "greaterThan",
        supportedFieldTypes = {BigDecimal.class, LocalDate.class},
        supportedValueTypes = {BigDecimal.class, LocalDate.class},
        description = "Checks if a field is strictly greater than to the given value"
)
public class GreaterThanOperator implements GenericOperator {
    @SuppressWarnings("unchecked")
    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null for greaterThan operator");
        }

        return ((Field<Object>) field).gt(value);    }
}
