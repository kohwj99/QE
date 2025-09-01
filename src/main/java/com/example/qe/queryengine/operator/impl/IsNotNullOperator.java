package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "isNotNull",
        supportedFieldTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        supportedValueTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        description = "Checks if a field is not null"
)
public class IsNotNullOperator implements GenericOperator {

    @Override
    public Condition apply(Field<?> field, Object value) {
        // value is ignored because not-null check doesn’t need a value
        return field.isNotNull();
    }
}
