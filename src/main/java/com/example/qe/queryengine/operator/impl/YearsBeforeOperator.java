package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "yearsBefore",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if today is a given number of years before a date field"
)
public class YearsBeforeOperator implements GenericOperator {
    @Override
    public Condition apply(Field<?> field, Object value) {
        if (value == null) {
            throw new NullPointerException("Year value cannot be null");
        }

        if (!LocalDate.class.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException(
                    "YearsBeforeOperator only supports LocalDate fields, but got: " + field.getType()
            );
        }

        LocalDate targetDate = LocalDate.now().plusYears(((BigDecimal) value).longValue());
        Field<LocalDate> dateOnlyField = DSL.field("CAST({0} AS DATE)", LocalDate.class, field);

        return dateOnlyField.eq(targetDate);
    }
}
