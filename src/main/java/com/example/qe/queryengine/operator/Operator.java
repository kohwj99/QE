package com.example.qe.queryengine.operator;

import org.jooq.Condition;
import org.jooq.Field;

public interface Operator {
    Condition apply(Field<?> field, Object value);
}