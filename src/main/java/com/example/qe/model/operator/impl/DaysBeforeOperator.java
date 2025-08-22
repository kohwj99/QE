package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "daysBefore",
        types = {BigDecimal.class},
        description = "Checks if a date field is a specified number of days before today"
)
@SuppressWarnings("rawtypes")
public class DaysBeforeOperator implements GenericOperator<BigDecimal> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, BigDecimal days) {
        LocalDate targetDate = LocalDate.now().minusDays(days.longValue());
        return ((Field<LocalDate>) field).eq(targetDate);
    }
}