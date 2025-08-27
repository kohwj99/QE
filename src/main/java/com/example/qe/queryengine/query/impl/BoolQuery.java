package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("BoolQuery")
public class BoolQuery extends FieldQuery<? , Boolean> {
    @JsonCreator
    public BoolQuery(@JsonProperty("column") String column,
                     @JsonProperty("operatorName") String operatorName,
                     @JsonProperty("value") Boolean value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<Boolean> getValueClass() {
        return Boolean.class;
    }
}
