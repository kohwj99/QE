package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@OperatorAnnotation(
        value = "daysAfter",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if today is a given number of days after a date field"
)
public class DaysAfterOperator implements GenericOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Day value cannot be null");
        }

        if (!(BigDecimal.class.isAssignableFrom(value.getClass()))) {
            throw new InvalidQueryException(
                    "DaysBeforeOperator requires a numeric value (BigDecimal), but got: " + value.getClass()
            );
        }

        // Ensure only LocalDate or LocalDateTime fields are supported
        if (!(LocalDate.class.isAssignableFrom(field.getType()) ||
                LocalDateTime.class.isAssignableFrom(field.getType()))) {
            throw new IllegalArgumentException(
                    "DaysBeforeOperator only supports LocalDate or LocalDateTime fields, but got: " + field.getType()
            );
        }

        long days = ((BigDecimal) value).longValue();
        LocalDate targetDate = LocalDate.now().minusDays(days);
        return DSL.condition("CAST({0} AS DATE) = {1}", field, targetDate);
    }
}