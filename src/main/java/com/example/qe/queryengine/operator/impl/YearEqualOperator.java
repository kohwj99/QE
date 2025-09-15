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
        value = "yearEqual",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the year component of a date field equals the specified year"
)
public class YearEqualOperator implements GenericOperator, RunConditionOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new InvalidQueryException("Year value cannot be null");
        }

        if (!LocalDate.class.isAssignableFrom(field.getType())) {
            throw new InvalidQueryException(
                    "YearEqualOperator only supports LocalDate fields, but got: " + field.getType()
            );
        }
        BigDecimal year = (BigDecimal) value;
        Field<Integer> yearField = DSL.field("YEAR(CAST({0} AS DATE))", Integer.class, field);
        return yearField.eq(year.intValue());
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {
        LocalDate date = LocalDate.parse((String) placeholder);
        BigDecimal year = (BigDecimal) value;
        if (date.getYear() == year.intValue()) {
            return DSL.condition("1 = 1");
        }
        return DSL.condition("1 = 0");
    }
}
