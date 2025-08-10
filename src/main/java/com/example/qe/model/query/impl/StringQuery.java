package com.example.qe.model.query.impl;

import com.example.qe.model.query.FieldQuery;
import org.jooq.Condition;
import org.jooq.DSLContext;

public class StringQuery extends FieldQuery<String> {
    @Override
    public Condition toCondition(DSLContext dsl) {
        return null;
    }
}
