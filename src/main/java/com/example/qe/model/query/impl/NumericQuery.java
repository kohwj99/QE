package com.example.qe.model.query.impl;

import com.example.qe.model.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.math.BigDecimal;

@JsonTypeName("NumericQuery")
public class NumericQuery extends FieldQuery<BigDecimal, BigDecimal> {

    @JsonCreator
    public NumericQuery(@JsonProperty("column") String column,
                       @JsonProperty("operatorName") String operatorName,
                       @JsonProperty("value") BigDecimal value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<BigDecimal> getFieldClass() {
        return BigDecimal.class;
    }

    @Override
    protected Class<BigDecimal> getValueClass() {
        return BigDecimal.class;
    }
}
