package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.CustomOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "monthsBefore",
        types = {BigDecimal.class},
        description = "Checks if a date field is a specified number of months before today"
)
public class MonthsBeforeOperator implements CustomOperator<BigDecimal> {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal months) {
        LocalDate targetDate = LocalDate.now().minusMonths(months.longValue());
        return DSL.condition("{0} = {1}", field, targetDate);
    }
}