package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@OperatorAnnotation(
        value = "monthsBefore",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if today is a given number of months before a date field"
)
public class MonthsBeforeOperator implements GenericOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new NullPointerException("Day value cannot be null");
        }

        if (!(LocalDate.class.isAssignableFrom(field.getType()) ||
                LocalDateTime.class.isAssignableFrom(field.getType()))) {
            throw new IllegalArgumentException(
                    "MonthsAfterOperator only supports LocalDate or LocalDateTime fields, but got: " + field.getType()
            );
        }

        int months = ((BigDecimal) value).intValue();
        LocalDate targetDate = LocalDate.now().plusMonths(months);

        // Cast field to DATE to ignore time portion
        Field<LocalDate> dateOnlyField = DSL.field("CAST({0} AS DATE)", LocalDate.class, field);

        return dateOnlyField.eq(targetDate);
    }
}