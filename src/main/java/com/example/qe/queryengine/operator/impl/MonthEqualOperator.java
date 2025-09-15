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
        value = "monthEqual",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the month component of a date field equals the specified month"
)
public class MonthEqualOperator implements GenericOperator, RunConditionOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Month value cannot be null");
        }
        if (!(LocalDate.class.isAssignableFrom(field.getType()))) {
            throw new InvalidQueryException(
                    "MonthEqualOperator only supports LocalDate field, but got: " + field.getType()
            );
        }
        int expectedMonth = ((BigDecimal) value).intValue();
        Field<Integer> monthField = DSL.field("MONTH(CAST({0} AS DATE))", Integer.class, field);
        return monthField.eq(expectedMonth);
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {
        LocalDate date = LocalDate.parse((String) placeholder);
        BigDecimal month = (BigDecimal) value;
        if (date.getMonthValue() == month.intValue()) {
            return DSL.condition("1 = 1");
        }
        return DSL.condition("1 = 0");
    }
}
