package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.CustomOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;

@OperatorAnnotation(
        value = "dayEqual",
        types = {BigDecimal.class},
        description = "Checks if the day component of a date field equals the specified day"
)
public class DayEqualOperator implements CustomOperator<BigDecimal> {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal day) {
        return DSL.day(field).eq(day.intValue());
    }
}
