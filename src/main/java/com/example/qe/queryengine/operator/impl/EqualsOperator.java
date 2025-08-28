package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.Operator;
import com.example.qe.queryengine.operator.OperatorAnnotation;
import org.jooq.Condition;
import org.jooq.Field;

import java.math.BigDecimal;
import java.time.LocalDate;

@OperatorAnnotation(
        value = "equals",
        supportedFieldTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        supportedValueTypes = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
        description = "Checks if a field equals the given value"
)
public class EqualsOperator implements Operator {

    @Override
    public Condition apply(Field<?> jooqField, Object value) {
        // jOOQ automatically handles proper casting for eq()

        @SuppressWarnings("unchecked")
        Field<Object> castedField = (Field<Object>) jooqField;
        return castedField.eq(value);
    }
}
