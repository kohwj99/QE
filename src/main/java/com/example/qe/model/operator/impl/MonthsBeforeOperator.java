package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "monthsBefore",
        types = {BigDecimal.class},
        description = "Checks if a date field is a specified number of months before today"
)
@SuppressWarnings("rawtypes")
public class MonthsBeforeOperator implements GenericOperator<BigDecimal> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, BigDecimal months) {
        Field<LocalDate> dateField = field;
        LocalDate targetDate = LocalDate.now().minusMonths(months.longValue());
        return dateField.eq(targetDate);
    }
}
