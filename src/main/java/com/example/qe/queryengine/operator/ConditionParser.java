package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.QueryEngineException;
import com.example.qe.queryengine.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ConditionParser {

    private final OperatorFactory operatorFactory;
    private final ObjectMapper objectMapper;
    private final DSLContext dsl;

    public ConditionParser(OperatorFactory operatorFactory, DSLContext dsl) {
        this.operatorFactory = operatorFactory;
        this.dsl = dsl;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Condition parseJsonToCondition(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new QueryEngineException("JSON cannot be null or empty");
        }

        Query query;
        try {
            query = objectMapper.readValue(json, Query.class);
        } catch (JsonProcessingException ex) {
            throw new QueryEngineException("Failed to convert JSON to Query class with error: "+ ex.getMessage(), ex);
        }

        try {
            query.validate();
        } catch (IllegalArgumentException ex) {
            throw new QueryEngineException("Query Validation failed with error: "+ ex.getMessage(), ex);
        }

        //TODO possible to do a check here for any issues?
        return query.toCondition(dsl, operatorFactory);
    }
}
