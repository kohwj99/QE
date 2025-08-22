package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "yearEqual",
        types = {BigDecimal.class},
        description = "Checks if the year component of a date field equals the specified year"
)
@SuppressWarnings("rawtypes")
public class YearEqualOperator implements GenericOperator<BigDecimal> {
    @Override
    @SuppressWarnings("unchecked")
    public Condition apply(Field field, BigDecimal year) {
        Field<LocalDate> dateField = field;
        return DSL.year(dateField).eq(year.intValue());
    }
}
