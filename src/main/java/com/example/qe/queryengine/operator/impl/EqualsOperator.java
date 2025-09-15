package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.RunConditionOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "equals",
        supportedFieldTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        supportedValueTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        description = "Checks if a field equals the given value"
)
public class EqualsOperator implements GenericOperator, RunConditionOperator {

    @SuppressWarnings("unchecked")
    @Override
    public Condition apply(Field<?> field, Object value) {

        if (value == null) {
            return field.isNull();
        }
        return ((Field<Object>) field).eq(value);
    }

    @Override
    public Condition evaluate(Object placeholder, Object value) {

        if (placeholder.equals(value)) {
            return DSL.condition("1 = 1");
        }
        return DSL.condition("1 = 0");
    }
}
