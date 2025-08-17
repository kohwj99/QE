package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.time.LocalDate;

@OperatorAnnotation(
        value = "daysAfter",
        types = {LocalDate.class},
        description = "Checks if a date field is a specified number of days after today"
)
@SuppressWarnings("rawtypes")
public class DaysAfterOperator implements GenericOperator<Integer> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, Integer days) {
        Field<LocalDate> dateField = field;
        LocalDate targetDate = LocalDate.now().plusDays(days);
        return dateField.eq(targetDate);
    }
}
