package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.math.BigDecimal;

@JsonTypeName("NumericQuery")
public class NumericQuery extends FieldQuery {
    @JsonCreator
    public NumericQuery(@JsonProperty("column") String column,
                        @JsonProperty("operator") String operator,
                        @JsonProperty("value") BigDecimal value) {
        super(column, operator, value);
    }

    @Override
    protected Class<?> getFieldClass() {
        return BigDecimal.class;
    }
}