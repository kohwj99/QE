package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.CustomOperator;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayOfMonth",
        types = {BigDecimal.class},
        description = "Checks if the day of the month component of a date field equals the specified day"
)
public class DayOfMonthOperator implements CustomOperator<BigDecimal> {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal day) {
        return DSL.day(field).eq(day.intValue());
    }
}
