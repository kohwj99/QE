//package com.example.qe.model.operator.impl;
//
//import com.example.qe.annotation.OperatorAnnotation;
//import com.example.qe.model.operator.GenericOperator;
//import org.jooq.Condition;
//import org.jooq.Field;
//
//import java.time.LocalDate;
//
//@OperatorAnnotation(
//        value = "daysBefore",
//        types = {Integer.class},
//        description = "Checks if a date field is before (current date minus given days)"
//)
//public class DaysBeforeOperator implements GenericOperator<Integer> {
//
//    @Override
//    public Condition apply(Field<Integer> field, Integer days) {
//        // This operator applies to date fields; we'll need to cast properly in usage
//        // Example assumes field is a LocalDate field, so we cast:
//        Field<LocalDate> dateField = (Field<LocalDate>) field;
//
//        LocalDate cutoffDate = LocalDate.now().minusDays(days);
//        return dateField.lessThan(cutoffDate);
//    }
//}
