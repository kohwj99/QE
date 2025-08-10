package com.example.qe.model.query.impl;

import com.example.qe.model.query.Query;
import org.jooq.Condition;
import org.jooq.DSLContext;

public class DateQuery implements Query {
    @Override
    public Condition toCondition(DSLContext dsl) {
        return null;
    }
}
