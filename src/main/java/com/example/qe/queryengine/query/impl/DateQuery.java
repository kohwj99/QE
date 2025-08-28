package com.example.qe.queryengine.query.impl;

import com.example.qe.queryengine.query.FieldQuery;
import com.example.qe.queryengine.query.deserializer.DateQueryDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
@JsonTypeName("DateQuery")
public class DateQuery extends FieldQuery {

    @JsonCreator
    public DateQuery(@JsonProperty("column") String column,
                     @JsonProperty("operatorName") String operatorName,
                     @JsonProperty("value") @JsonDeserialize(using = DateQueryDeserializer.class)Object value) {
        super(column, operatorName, value);
    }

    @Override
    protected Class<LocalDate> getFieldClass() {
        return LocalDate.class;
    }
}
