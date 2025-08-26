package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("StringQuery")
public class StringQuery extends FieldQuery<String> {

    @JsonCreator
    public StringQuery(@JsonProperty("column") String column,
                       @JsonProperty("operatorName") String operatorName,
                       @JsonProperty("value") String value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<String> getValueClass() {
        return String.class;
    }
}
