package com.example.qe.queryengine.operator;

public interface RunConditionOperator {

    Boolean evaluate(Object customFieldValue, Object conditionValue);
}
