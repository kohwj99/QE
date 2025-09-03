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
        value = "monthsAfter",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if today is a given number of months after a date field"
)
public class MonthsAfterOperator implements GenericOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Month value cannot be null");
        }

        if (!(LocalDate.class.isAssignableFrom(field.getType()) ||
                LocalDateTime.class.isAssignableFrom(field.getType()))) {
            throw new InvalidQueryException(
                    "MonthsAfterOperator only supports LocalDate or LocalDateTime fields, but got: " + field.getType()
            );
        }

        int months = ((BigDecimal) value).intValue();
        LocalDate targetDate = LocalDate.now().minusMonths(months);

        // Cast field to DATE to ignore time portion
        Field<LocalDate> dateOnlyField = DSL.field("CAST({0} AS DATE)", LocalDate.class, field);

        return dateOnlyField.eq(targetDate);
    }
}
