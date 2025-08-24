package com.example.qe.model.operator;
import org.jooq.Condition;
import org.jooq.Field;

//public interface GenericOperator<T> {
//    /**
//     * Apply this operator to a DSL field and a value, producing a Condition.
//     */
//    Condition apply(Field<T> field, T value);
//}


public interface GenericOperator<T> extends Operator<T,T>{
    /**
     * Apply this operator to a DSL field and a value, producing a Condition.
     */
    @Override
    Condition apply(Field<T> field, T value);
}