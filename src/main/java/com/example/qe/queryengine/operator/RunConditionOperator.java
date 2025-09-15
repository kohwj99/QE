package com.example.qe.queryengine.operator;

import org.jooq.Condition;

@FunctionalInterface
public interface RunConditionOperator {
    Condition evaluate(Object placeholder, Object value);
}
