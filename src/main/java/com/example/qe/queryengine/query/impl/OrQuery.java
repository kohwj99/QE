package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.query.CompositeQuery;
import com.example.qe.queryengine.query.Query;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.jooq.Condition;
import java.util.List;

@JsonTypeName("OrQuery")
public class OrQuery extends CompositeQuery {
    @JsonCreator
    public OrQuery(@JsonProperty("children") List<Query> children) {
        super(children);
    }

    @Override
    protected Condition combineConditions(List<Condition> conditions) {
        return conditions.stream()
                .reduce(Condition::or)
                .orElseThrow(() -> new InvalidQueryException("OrQuery must have at least one child"));
    }
}
