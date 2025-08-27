package com.example.qe.queryengine.operator;

import org.jooq.Condition;
import org.jooq.Field;

/**
 * CustomOperator interface for defining operators that can be applied to fields of various types.
 *
 * @param <F> the field type
 * @param <V> the value type
 */
public interface CustomOperator<F,V> {

    Condition apply(Field<F> field, V value);
}
