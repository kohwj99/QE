package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;

@OperatorAnnotation(
        value = "monthEqual",
        types = {LocalDate.class},
        description = "Checks if the month component of a date field equals the specified month"
)
@SuppressWarnings("rawtypes")
public class MonthEqualOperator implements GenericOperator<Integer> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, Integer month) {
        Field<LocalDate> dateField = field;
        return DSL.month(dateField).eq(month);
    }
}
