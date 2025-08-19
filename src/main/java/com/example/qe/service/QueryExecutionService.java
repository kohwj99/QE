package com.example.qe.service;

import com.example.qe.model.query.Query;
import com.example.qe.util.QueryExecutionContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryExecutionService {

    private final QueryExecutionContext context;
    private final ObjectMapper objectMapper;

    @Autowired
    public QueryExecutionService(QueryExecutionContext context) {
        this.context = context;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Parses JSON string to a Query object and converts it to a JOOQ Condition
     * @param json JSON string representing the query
     * @return JOOQ Condition object for SQL generation
     * @throws JsonProcessingException if JSON parsing fails
     */
    public Condition parseJsonToCondition(String json) throws JsonProcessingException {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON cannot be null or empty");
        }

        // Create a default DSLContext for condition generation
        DSLContext dsl = DSL.using(SQLDialect.DEFAULT);

        // Parse JSON to Query object
        Query query = objectMapper.readValue(json, Query.class);

        // Convert Query to JOOQ Condition
        return query.toCondition(dsl, context);
    }

    /**
     * Executes a Query object with a provided DSLContext
     * @param query Query object to execute
     * @param dsl DSLContext for SQL generation
     * @return JOOQ Condition object
     */
    public Condition executeQuery(Query query, DSLContext dsl) {
        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }
        if (dsl == null) {
            throw new IllegalArgumentException("DSLContext cannot be null");
        }

        return query.toCondition(dsl, context);
    }

    /**
     * Parses JSON and executes with provided DSLContext
     * @param json JSON string representing the query
     * @param dsl DSLContext for SQL generation
     * @return JOOQ Condition object
     * @throws JsonProcessingException if JSON parsing fails
     */
    public Condition parseAndExecute(String json, DSLContext dsl) throws JsonProcessingException {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON cannot be null or empty");
        }
        if (dsl == null) {
            throw new IllegalArgumentException("DSLContext cannot be null");
        }

        Query query = objectMapper.readValue(json, Query.class);
        return query.toCondition(dsl, context);
    }

    /**
     * Converts a Query object to SQL string for debugging/logging
     * @param query Query object to convert
     * @return SQL string representation
     */
    public String queryToSql(Query query) {
        DSLContext dsl = DSL.using(SQLDialect.DEFAULT);
        Condition condition = query.toCondition(dsl, context);
        return dsl.renderInlined(condition);
    }

    /**
     * Parses JSON to SQL string for debugging/logging
     * @param json JSON string representing the query
     * @return SQL string representation
     * @throws JsonProcessingException if JSON parsing fails
     */
    public String jsonToSql(String json) throws JsonProcessingException {
        Query query = objectMapper.readValue(json, Query.class);
        return queryToSql(query);
    }

    /**
     * Validates if JSON string can be parsed to a valid Query
     * @param json JSON string to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidQueryJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return false;
            }
            objectMapper.readValue(json, Query.class);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    // Getter for accessing the context (useful for testing)
    public QueryExecutionContext getContext() {
        return context;
    }
}
