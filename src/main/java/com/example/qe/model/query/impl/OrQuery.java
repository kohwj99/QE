package com.example.qe.model.query.impl;

import com.example.qe.model.query.CompositeQuery;
import com.example.qe.model.query.Query;
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
                .orElseThrow(() -> new IllegalStateException("OrQuery must have at least one child"));
    }
}
