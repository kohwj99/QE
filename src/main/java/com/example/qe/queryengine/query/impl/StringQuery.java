package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.example.qe.queryengine.query.ValueType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("StringQuery")
public class StringQuery extends FieldQuery {

    @JsonCreator
    public StringQuery(@JsonProperty("column") String column,
                       @JsonProperty("operatorName") String operatorName,
                       @JsonProperty("value") String value,
                       @JsonProperty("valueType") ValueType valueType) {
        super(column, operatorName, value, valueType);
    }

    @Override
    protected Class<String> getFieldClass() {
        return String.class;
    }
}
