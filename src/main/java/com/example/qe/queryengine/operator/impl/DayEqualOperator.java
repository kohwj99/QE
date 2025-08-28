package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayEqual",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the day component of a date field equals the specified day of the month"
)
public class DayEqualOperator implements GenericOperator {
    @Override
    public Condition apply(Field<?> field, Object day) {
        int dayValue = ((BigDecimal) day).intValue();
        return DSL.day(field).eq(dayValue);
    }
}
