package com.example.qe.queryengine.operator;
import org.jooq.Condition;
import org.jooq.Field;

public interface GenericOperator<T> extends CustomOperator<T,T> {
    /**
     * Apply this operator to a DSL field and a value, producing a Condition.
     */
    Condition apply(Field<T> field, T value);
}
