package com.example.qe.model.query;

import com.example.qe.util.QueryExecutionContext;
import org.jooq.Condition;
import org.jooq.DSLContext;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CompositeQuery implements Query {

    List<Query> children;

    public CompositeQuery(List<Query> children) {
        this.children = children;
    }

    public List<Query> getChildren() {
        return children;
    }

    @Override
    public Condition toCondition(DSLContext dsl, QueryExecutionContext context) {
        // Convert all child queries to conditions recursively
        List<Condition> childConditions = children.stream()
                .map(child -> child.toCondition(dsl, context))
                .collect(Collectors.toList());

        // Combine them using subclass-specific logic (AND / OR)
        return combineConditions(childConditions);
    }

    protected abstract Condition combineConditions(List<Condition> conditions);
}

