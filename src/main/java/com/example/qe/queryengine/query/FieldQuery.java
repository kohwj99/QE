package com.example.qe.queryengine.query;

import com.example.qe.queryengine.operator.Operator;
import com.example.qe.queryengine.operator.OperatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;

import static org.jooq.impl.DSL.field;

@Setter
@Getter
public abstract class FieldQuery implements Query {

    // JSON-friendly fields
    protected String column;
    protected String operator;
    protected Object value; // Nullable for IS NULL / IS NOT NULL

    protected FieldQuery() {
        // Default constructor for JSON deserialization
    }

    protected FieldQuery(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        Class<?> fieldType = getFieldClass();

        // Resolve operator + expected valueType
        OperatorFactory.ResolvedOperator resolved = operatorFactory.resolveWithDynamicValueType(operator, fieldType);

        // Build the jOOQ field
        Field<?> jooqField = field(column, fieldType);

        // Convert value to expected type
        Object castedValue = convertValue(value, resolved.valueType());

        // Apply operator
        Operator op = resolved.operator();
        return op.apply(jooqField, castedValue);
    }

    /**
     * Convert the raw JSON value into the type the operator expects.
     */
    protected Object convertValue(Object rawValue, Class<?> targetType) {
        if (rawValue == null) {
            return null;
        }

        // Already the right type
        if (targetType.isInstance(rawValue)) {
            return rawValue;
        }

        // Basic conversions
        if (targetType == Integer.class) {
            return Integer.valueOf(rawValue.toString());
        } else if (targetType == Long.class) {
            return Long.valueOf(rawValue.toString());
        } else if (targetType == Double.class) {
            return Double.valueOf(rawValue.toString());
        } else if (targetType == Boolean.class) {
            return Boolean.valueOf(rawValue.toString());
        } else if (targetType == String.class) {
            return rawValue.toString();
        } else if (targetType == java.time.LocalDate.class) {
            return java.time.LocalDate.parse(rawValue.toString());
        } else if (targetType == java.time.LocalDateTime.class) {
            return java.time.LocalDateTime.parse(rawValue.toString());
        }

        throw new IllegalArgumentException("Cannot convert value '" + rawValue + "' to type " + targetType.getSimpleName());
    }

    protected abstract Class<?> getFieldClass();
}
