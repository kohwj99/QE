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
        value = "dayOfWeek",
        supportedFieldTypes = {LocalDate.class},
        supportedValueTypes = {BigDecimal.class},
        description = "Checks if the day component of a date field equals the specified day of the week, where Monday=1 and Sunday=7"
)
public class DayOfWeekOperator implements GenericOperator, RunConditionOperator {
    @Override
    public Condition apply(Field<?> field, Object day) {
        if (day == null) {
            throw new InvalidQueryException("Day value cannot be null");
        }

        if (!LocalDate.class.isAssignableFrom(field.getType())) {
            throw new InvalidQueryException(
                    "DayOfWeekOperator only supports LocalDate fields, but got: " + field.getType()
            );
        }
        int expectedDay = ((BigDecimal) day).intValue();
        // Remap to Monday=1 .. Sunday=7
        Field<Integer> dayOfWeekMapped = DSL.field(
                "((datepart(weekday, {0}) + @@DATEFIRST - 2) % 7) + 1",
                Integer.class,
                field
        );
        return dayOfWeekMapped.eq(expectedDay);
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {
        // placeholder is expected to be a date
        // value is 1 - 7, which is mon - sun
        LocalDate date = LocalDate.parse((String) placeholder);
        Integer dayOfWeek = date.getDayOfWeek().getValue();
        System.out.println(dayOfWeek);
        if (dayOfWeek.equals(value)) {
            return DSL.condition("1 = 1");
        }
        return DSL.condition("1 = 0");
    }
}
