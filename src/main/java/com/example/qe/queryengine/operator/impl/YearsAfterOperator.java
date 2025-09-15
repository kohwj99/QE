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
        value = "yearsAfter",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if today is a given number of years after a date field"
)
public class YearsAfterOperator implements GenericOperator, RunConditionOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Year value cannot be null");
        }
        if (!LocalDate.class.isAssignableFrom(field.getType())) {
            throw new InvalidQueryException(
                    "YearsAfterOperator only supports LocalDate fields, but got: " + field.getType()
            );
        }
        LocalDate targetDate = LocalDate.now().minusYears(((BigDecimal) value).longValue());
        Field<LocalDate> dateOnlyField = DSL.field("CAST({0} AS DATE)", LocalDate.class, field);
        return dateOnlyField.eq(targetDate);
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {
        LocalDate date = LocalDate.parse((String) placeholder);
        BigDecimal years = (BigDecimal) value;
        LocalDate targetDate = LocalDate.now().minusYears(years.intValue());
        return date.equals(targetDate) ? DSL.condition("1 = 1") : DSL.condition("1 = 0");
    }
}
