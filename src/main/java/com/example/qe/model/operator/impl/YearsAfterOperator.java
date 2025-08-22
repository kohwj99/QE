package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "yearsAfter",
        types = {BigDecimal.class},
        description = "Checks if a date field is a specified number of years after today"
)
@SuppressWarnings("rawtypes")
public class YearsAfterOperator implements GenericOperator<BigDecimal> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, BigDecimal years) {
        Field<LocalDate> dateField = field;
        LocalDate targetDate = LocalDate.now().plusYears(years.longValue());
        return dateField.eq(targetDate);
    }
}
