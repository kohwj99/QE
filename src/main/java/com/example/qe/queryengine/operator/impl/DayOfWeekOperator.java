package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "dayOfWeek",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the day component of a date field equals the specified day of the week, where Monday=1 and Sunday=7"
)
public class DayOfWeekOperator implements GenericOperator {
    @Override
    public Condition apply(Field<?> field, Object day) {
        if (day == null) {
            throw new NullPointerException("Day value cannot be null");
        }

        if (!LocalDate.class.isAssignableFrom(field.getType())) {
            throw new IllegalArgumentException(
                    "DayOfWeekOperator only supports LocalDate fields, but got: " + field.getType()
            );
        }

        int expectedDay = ((BigDecimal) day).intValue();

        // SQL Server: DATEPART(WEEKDAY, field) returns Sunday=1 .. Saturday=7
        // Remap to Monday=1 .. Sunday=7
        Field<Integer> dayOfWeekMapped = DSL.field(
                "((datepart(weekday, {0}) + @@DATEFIRST - 2) % 7) + 1",
                Integer.class,
                field
        );

        return dayOfWeekMapped.eq(expectedDay);
    }
}
