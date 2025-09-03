package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.query.CompositeQuery;
import com.example.qe.queryengine.query.Query;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jooq.Condition;

import java.util.List;

@JsonTypeName("AndQuery")
public class AndQuery extends CompositeQuery {
    @JsonCreator
    public AndQuery(@JsonProperty("children") List<Query> children) {
        super(children);
    }

    @Override
    protected Condition combineConditions(List<Condition> conditions) {
        return conditions.stream()
                .reduce(Condition::and)
                .orElseThrow(() -> new InvalidQueryException("AndQuery requires at least one child"));
    }
}
