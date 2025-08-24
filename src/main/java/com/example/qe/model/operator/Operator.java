package com.example.qe.model.operator;

import org.jooq.Condition;
import org.jooq.Field;

public interface Operator<F,T> {
    /**
     * Apply this operator to a DSL field and a value, producing a Condition.
     */
    Condition apply(Field<F> field, T value);
}
