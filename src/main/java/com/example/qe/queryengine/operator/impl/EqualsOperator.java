package com.example.qe.queryengine.operator.impl;

import com.example.qe.queryengine.operator.OperatorAnnotation;
import com.example.qe.queryengine.operator.GenericOperator;
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
public class EqualsOperator implements GenericOperator {

    @SuppressWarnings("unchecked")
    @Override
    public Condition apply(Field<?> field, Object value) {

        if (value == null) {
            return field.isNull();
        }

        Class<?> fieldType = field.getType();


        if (fieldType == String.class && !(value instanceof String)) {
            throw new IllegalArgumentException("String field requires String value");
        }
        if (fieldType == Boolean.class && !(value instanceof Boolean)) {
            throw new IllegalArgumentException("Boolean field requires Boolean value");
        }
        if (fieldType == LocalDate.class && !(value instanceof LocalDate)) {
            throw new IllegalArgumentException("LocalDate field requires LocalDate value");
        }
        if (fieldType == BigDecimal.class && !(value instanceof BigDecimal)) {
            throw new IllegalArgumentException("BigDecimal field requires BigDecimal value");
        }

        return ((Field<Object>) field).eq(value);
    }
}
