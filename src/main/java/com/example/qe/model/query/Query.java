package com.example.qe.model.query;
import org.jooq.Condition;
import org.jooq.DSLContext;

public interface Query {
    Condition toCondition(DSLContext dsl);
    // This method should be implemented to convert the query into a jOOQ Condition
}
