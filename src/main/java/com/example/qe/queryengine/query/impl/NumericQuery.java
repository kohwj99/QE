package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.example.qe.queryengine.query.ValueType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.math.BigDecimal;

@JsonTypeName("NumericQuery")
public class NumericQuery extends FieldQuery {

    @JsonCreator
    public NumericQuery(@JsonProperty("column") String column,
                       @JsonProperty("operatorName") String operatorName,
                        @JsonProperty("value") Object value,
                        @JsonProperty("valueType") ValueType valueType) {
        super(column, operatorName, value, valueType);
    }

    @Override
    protected Class<BigDecimal> getFieldClass() {
        return BigDecimal.class;
    }
}
