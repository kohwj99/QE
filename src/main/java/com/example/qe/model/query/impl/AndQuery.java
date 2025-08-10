package com.example.qe.model.query.impl;

import com.example.qe.model.query.CompositeQuery;
import org.jooq.Condition;
import org.jooq.DSLContext;

public class AndQuery extends CompositeQuery {
    @Override
    public Condition toCondition(DSLContext dsl) {
        return null;
    }
}
