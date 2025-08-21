package com.example.qe.service;

import com.example.qe.model.query.Query;
import com.example.qe.util.QueryExecutionContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueryExecutionService {

    @Getter
    private final QueryExecutionContext context;
    private final ObjectMapper objectMapper;
    private final DSLContext dsl;
    private final ReplacementService replacementService;

    public QueryExecutionService(QueryExecutionContext context,
                                @Value("${spring.jooq.sql-dialect:DEFAULT}") String sqlDialect,
                                ReplacementService replacementService) {
        this.context = context;
        this.replacementService = replacementService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        SQLDialect dialect = SQLDialect.valueOf(sqlDialect.toUpperCase());
        this.dsl = DSL.using(dialect);
    }

    public Condition parseJsonToCondition(String json) throws JsonProcessingException {
        System.out.println("Original JSON: " + json);
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON cannot be null or empty");
        }

        // STEP 1: Replace placeholders in JSON before deserialization
        String processedJson = replacementService.processJsonPlaceholders(json);
        System.out.println("Processed JSON: " + processedJson);

        // STEP 2: Deserialize the processed JSON to Query object
        Query query = objectMapper.readValue(processedJson, Query.class);

        // STEP 3: Generate condition and SQL for debugging
        Condition condition = query.toCondition(dsl, context);
        String sql = dsl.renderInlined(dsl.select().from("your_table").where(condition));
        System.out.println("Generated SQL: " + sql);

        return condition;
    }
}
