package com.example.qe.model.query.impl;

import com.example.qe.model.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("IntQuery")
public class IntQuery extends FieldQuery<Integer> {

    @JsonCreator
    public IntQuery(@JsonProperty("column") String column,
                    @JsonProperty("operatorName")String operatorName,
                    @JsonProperty("value")Integer value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<Integer> getValueClass() {
        return Integer.class;
    }
}
