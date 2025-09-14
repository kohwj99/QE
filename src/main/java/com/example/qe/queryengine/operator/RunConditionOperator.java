package com.example.qe.queryengine.operator;

import org.jooq.Condition;
import org.jooq.Field;

@FunctionalInterface
public interface RunConditionOperator {
    Condition evaluate(Object placeholder, Object value);
}
