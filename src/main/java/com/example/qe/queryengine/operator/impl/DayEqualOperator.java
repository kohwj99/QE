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
        value = "dayEqual",
        types = {BigDecimal.class},
        description = "Checks if the day component of a date field equals the specified day of the week, where Monday=1 and Sunday=7"
)
public class DayEqualOperator implements CustomOperator<LocalDate, BigDecimal> , RunConditionOperator {
    @Override
    public Condition apply(Field<LocalDate> field, BigDecimal day) {
        // Align DB dayOfWeek() with ISO (Monday=1, Sunday=7)
        Field<Integer> adjustedDay = DSL.when(DSL.dayOfWeek(field).eq(1), 7)
                .otherwise(DSL.dayOfWeek(field).minus(1));
        return adjustedDay.eq(day.intValue());
    }

    @Override
    public Boolean evaluate(Object customFieldValue, Object conditionValue) {
        if (customFieldValue instanceof LocalDate date && conditionValue instanceof BigDecimal day) {
            return date.getDayOfWeek().getValue() == day.intValue();
        } else {
            throw new IllegalArgumentException("Invalid types for evaluation");
        }
    }
}
