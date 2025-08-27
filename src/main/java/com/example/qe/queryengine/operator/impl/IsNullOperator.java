//package com.example.qe.queryengine.operator.impl;
//import com.example.qe.queryengine.operator.OperatorAnnotation;
//import com.example.qe.queryengine.operator.GenericOperator;
//import org.jooq.Condition;
//import org.jooq.Field;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@OperatorAnnotation(
//        value = "isNull",
//        types = {String.class, BigDecimal.class, Boolean.class, LocalDate.class},
//        description = "Checks if a field is null"
//)
//public class IsNullOperator<T> implements GenericOperator<T> {
//
//    @Override
//    public Condition apply(Field<T> field, T value) {
//        // value is ignored because null check doesn’t need a value
//        return field.isNull();
//    }
//}
