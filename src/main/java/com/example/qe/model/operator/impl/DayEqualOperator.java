package com.example.qe.model.operator.impl;

import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.Operator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayEqual",
        types = {BigDecimal.class},
        description = "Checks if the day component of a date field equals the specified day"
)
public class DayEqualOperator implements Operator<LocalDate,BigDecimal> {

    @Override
    public Condition apply(Field<LocalDate> field, BigDecimal day) {
        return DSL.day(field).eq(day.intValue());    }
}
