package com.example.qe.queryengine.operator.impl;


import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "lessThan",
        supportedFieldTypes = {BigDecimal.class, LocalDate.class},
        supportedValueTypes = {BigDecimal.class, LocalDate.class},
        description = "Checks if a field is STRICTLY less than the given value"
)
public class LessThanOperator implements GenericOperator {
    @SuppressWarnings("unchecked")
    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Value cannot be null for lessThan operator");
        }

        return ((Field<Object>) field).lt(value);
    }
}
