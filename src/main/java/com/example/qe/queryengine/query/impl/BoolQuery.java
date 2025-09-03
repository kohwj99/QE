package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.example.qe.queryengine.query.ValueType;
import com.example.qe.queryengine.query.deserializer.BooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeName("BoolQuery")
public class BoolQuery extends FieldQuery {
    @JsonCreator
    public BoolQuery(@JsonProperty("column") String column,
                     @JsonProperty("operatorName") String operatorName,
                     @JsonProperty("value") @JsonDeserialize(using = BooleanDeserializer.class) Boolean value,
                         @JsonProperty("valueType") ValueType valueType){
        super(column, operatorName, value, valueType);
    }

    @Override
    protected Class<Boolean> getFieldClass() {
        return Boolean.class;
    }
}
