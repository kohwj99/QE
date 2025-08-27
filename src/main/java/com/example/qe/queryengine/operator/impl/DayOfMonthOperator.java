package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.CustomOperator;
import com.example.qe.queryengine.operator.RunConditionOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayOfMonth",
        types = {BigDecimal.class},
        description = "Checks if the day of the month component of a date field equals the specified day"
)
public class DayOfMonthOperator implements CustomOperator<BigDecimal>, RunConditionOperator {
    @Override
    public Condition applyToField(Field<?> field, BigDecimal day) {
        return DSL.day(field).eq(day.intValue());
    }

    @Override
    public Boolean evaluate(Object customFieldValue, Object conditionValue) {
        if (customFieldValue instanceof LocalDate date && conditionValue instanceof BigDecimal day) {
            return date.getDayOfMonth() == day.intValue();
        } else {
            throw new IllegalArgumentException("Invalid types for evaluation");
        }
    }
}
