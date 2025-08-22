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
        value = "daysBefore",
        types = {BigDecimal.class},
        description = "Checks if a date field is a specified number of days before today"
)
public class DaysBeforeOperator implements CustomOperator<BigDecimal> {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal days) {
        LocalDate targetDate = LocalDate.now().minusDays(days.longValue());
        return DSL.condition("{0} = {1}", field, targetDate);
    }
}