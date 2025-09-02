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
        value = "monthEqual",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the month component of a date field equals the specified month"
)
public class MonthEqualOperator implements GenericOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new NullPointerException("Month value cannot be null");
        }

        if (!(LocalDate.class.isAssignableFrom(field.getType()) ||
                LocalDateTime.class.isAssignableFrom(field.getType()))) {
            throw new IllegalArgumentException(
                    "MonthEqualOperator only supports LocalDate or LocalDateTime fields, but got: " + field.getType()
            );
        }

        int expectedMonth = ((BigDecimal) value).intValue();

        // ✅ Ensure only date part is considered
        Field<Integer> monthField = DSL.field("MONTH(CAST({0} AS DATE))", Integer.class, field);

        return monthField.eq(expectedMonth);
    }
}
