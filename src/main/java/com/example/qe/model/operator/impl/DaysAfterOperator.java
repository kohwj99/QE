package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.Operator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "daysAfter",
        types = {BigDecimal.class},
        description = "Checks if a date field is a specified number of days after today"
)
public class DaysAfterOperator implements Operator<LocalDate, BigDecimal> {
    @Override
    public Condition apply(Field<LocalDate> field, BigDecimal days) {
        LocalDate targetDate = LocalDate.now().plusDays(days.longValue());
        return DSL.condition("{0} = {1}", field, targetDate);
    }
}