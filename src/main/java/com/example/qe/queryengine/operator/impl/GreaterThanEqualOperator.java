package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "greaterThanEqual",
        supportedFieldTypes = {BigDecimal.class, LocalDate.class},
        supportedValueTypes = {BigDecimal.class, LocalDate.class},
        description = "Checks if a field is greater than or equal to the given value"
)
public class GreaterThanEqualOperator implements GenericOperator {
    @SuppressWarnings("unchecked")
    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Value cannot be null for greaterThanEqual operator");
        }

        return ((Field<Object>) field).ge(value);
    }
}
