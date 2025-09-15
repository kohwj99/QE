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
        value = "monthsBefore",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if today is a given number of months before a date field"
)
public class MonthsBeforeOperator implements GenericOperator, RunConditionOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Day value cannot be null");
        }

        if (!(LocalDate.class.isAssignableFrom(field.getType()) )) {
            throw new InvalidQueryException(
                    "MonthsBeforeOperator only supports LocalDate field, but got: " + field.getType()
            );
        }
        int months = ((BigDecimal) value).intValue();
        LocalDate targetDate = LocalDate.now().plusMonths(months);
        Field<LocalDate> dateOnlyField = DSL.field("CAST({0} AS DATE)", LocalDate.class, field);
        return dateOnlyField.eq(targetDate);
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {
        LocalDate date = LocalDate.parse((String) placeholder);
        BigDecimal months = (BigDecimal) value;
        LocalDate targetDate = LocalDate.now().plusMonths(months.intValue());
        return date.equals(targetDate) ? DSL.condition("1 = 1") : DSL.condition("1 = 0");
    }
}
