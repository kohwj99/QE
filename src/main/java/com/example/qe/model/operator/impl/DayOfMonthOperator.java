package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayOfMonth",
        types = {LocalDate.class},
        description = "Checks if the day of the month component of a date field equals the specified day"
)
@SuppressWarnings("rawtypes")
public class DayOfMonthOperator implements GenericOperator<Integer> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, Integer day) {
        Field<LocalDate> dateField = field;
        return DSL.day(dateField).eq(day);
    }
}
