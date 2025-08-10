package com.example.qe.model.query.impl;

import com.example.qe.model.query.FieldQuery;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;
@JsonTypeName("DateQuery")
public class DateQuery extends FieldQuery<java.time.LocalDate> {

    @JsonCreator
    public DateQuery(@JsonProperty("column") String column,
                     @JsonProperty("operatorName") String operatorName,
                     @JsonProperty("value") LocalDate value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<LocalDate> getValueClass() {
        return LocalDate.class;
    }
}
