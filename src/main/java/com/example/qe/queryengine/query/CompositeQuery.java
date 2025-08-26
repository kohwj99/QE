package com.example.qe.queryengine.query;

import com.example.qe.queryengine.operator.OperatorFactory;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class CompositeQuery implements Query {

    List<Query> children;

    public CompositeQuery(List<Query> children) {
        this.children = children;
    }

    @Override
    public Condition toCondition(DSLContext dsl, OperatorFactory operatorFactory) {
        // Convert all child queries to conditions recursively
        List<Condition> childConditions = children.stream()
                .map(child -> child.toCondition(dsl, operatorFactory))
                .collect(Collectors.toList());

        // Combine them using subclass-specific logic (AND / OR)
        return combineConditions(childConditions);
    }

    protected abstract Condition combineConditions(List<Condition> conditions);
}
