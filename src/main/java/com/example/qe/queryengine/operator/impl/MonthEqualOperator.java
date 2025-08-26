package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.CustomOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;

@OperatorAnnotation(
        value = "monthEqual",
        types = {BigDecimal.class},
        description = "Checks if the month component of a date field equals the specified month"
)
public class MonthEqualOperator implements CustomOperator<BigDecimal> {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal month) {
        return DSL.month(field).eq(month.intValue());
    }
}
