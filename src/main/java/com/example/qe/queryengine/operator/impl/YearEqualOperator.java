package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.CustomOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;

@OperatorAnnotation(
        value = "yearEqual",
        types = {BigDecimal.class},
        description = "Checks if the year component of a date field equals the specified year"
)
public class YearEqualOperator implements CustomOperator<BigDecimal> {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal year) {
        return DSL.year(field).eq(year.intValue());
    }
}
