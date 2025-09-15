package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.RunConditionOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayOfMonth",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the day of the month component of a date field equals the specified day, from 1 to 31 (depending on month and year)"
)
public class DayOfMonthOperator implements GenericOperator, RunConditionOperator {
    @Override
    public Condition apply(Field<?> field, Object day) {
        if (day == null) {
            throw new InvalidQueryException("Day value cannot be null");
        }

        if (!LocalDate.class.isAssignableFrom(field.getType())) {
            throw new InvalidQueryException(
                    "DayOfMonthOperator only supports LocalDate fields, but got: " + field.getType()
            );
        }
        int expectedDay = ((BigDecimal) day).intValue();
        Field<Integer> dayField = DSL.field("DAY(CAST({0} AS DATE))", Integer.class, field);
        return dayField.eq(expectedDay);
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {
        LocalDate date = LocalDate.parse((String) placeholder);
        Integer dayOfMonth = date.getDayOfMonth();
        Integer day = ((BigDecimal) value).intValue();
        if (dayOfMonth.equals(day)) {
            return DSL.condition("1 = 1");
        }
        return DSL.condition("1 = 0");
    }
}
