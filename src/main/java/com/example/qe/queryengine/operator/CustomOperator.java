package com.example.qe.queryengine.operator;

import org.jooq.Condition;
import org.jooq.Field;

public interface CustomOperator<T> extends GenericOperator<T> {
    /**
     * Apply this operator to a DSL field and a value, producing a Condition.
     *
     * @param field the field to apply the operator on
     * @param value the value to compare against
     * @return a Condition representing the operation
     */
    // Override with more flexible field type
    Condition applyToField(Field<?> field, T value);

    // Provide default implementation for GenericOperator method
    @Override
    default Condition apply(Field<T> field, T value) {
        // Delegate to the more flexible method
        return applyToField((Field<?>) field, value);
    }
}
