package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.time.LocalDate;

@OperatorAnnotation(
        value = "yearsBefore",
        types = {LocalDate.class},
        description = "Checks if a date field is a specified number of years before today"
)
@SuppressWarnings("rawtypes")
public class YearsBeforeOperator implements GenericOperator<Integer> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, Integer years) {
        Field<LocalDate> dateField = field;
        LocalDate targetDate = LocalDate.now().minusYears(years);
        return dateField.eq(targetDate);
    }
}
