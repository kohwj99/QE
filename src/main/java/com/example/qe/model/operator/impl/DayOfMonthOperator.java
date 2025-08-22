package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
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
@SuppressWarnings("rawtypes")
public class DayOfMonthOperator implements GenericOperator<BigDecimal> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, BigDecimal day) {
        Field<LocalDate> dateField = field;
        return DSL.day(dateField).eq(day.intValue());
    }
}
