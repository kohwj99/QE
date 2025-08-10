package com.example.qe.model.operator.impl;
import com.example.qe.annotation.OperatorAnnotation;
import com.example.qe.model.operator.GenericOperator;
import org.jooq.Condition;
import org.jooq.Field;

@OperatorAnnotation(
        value = "isNull",
        types = {Object.class},  // supports any type
        description = "Checks if a field is null"
)
public class IsNullOperator<T> implements GenericOperator<T> {

    @Override
    public Condition apply(Field<T> field, T value) {
        // value is ignored because null check doesn’t need a value
        return field.isNull();
    }
}
