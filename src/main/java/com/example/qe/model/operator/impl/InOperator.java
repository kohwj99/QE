//package com.example.qe.model.operator.impl;
//
//import com.example.qe.annotation.OperatorAnnotation;
//import com.example.qe.model.operator.GenericOperator;
//import org.jooq.Condition;
//import org.jooq.Field;
//
//import java.util.Collection;
//
//@OperatorAnnotation(
//        value = "in",
//        types = {Collection.class},
//        description = "Checks if a field value is in the given collection"
//)
//public class InOperator<T> implements GenericOperator<Collection<T>> {
//    @Override
//    public Condition apply(Field<T> field, Collection<T> value) {
//        return field.in(value);
//    }
//}
