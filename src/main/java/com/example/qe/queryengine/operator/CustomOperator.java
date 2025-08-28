package com.example.qe.queryengine.operator;

import org.jooq.Condition;
import org.jooq.Field;

public interface CustomOperator<F,V> {

//    Condition apply(Field<F> field, V value);

    Condition apply(Field<?> jooqField, Object castedValue);
}
