package com.example.qe.queryengine.operator;

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

    public Condition parseJsonToCondition(String json) throws JsonProcessingException {
        System.out.println("Original JSON: " + json);
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON cannot be null or empty");
        }
        System.out.println("Replacement Service: " + "Not used currently");
        System.out.println("1 ==============================================");
        String processedJson = json;
        // STEP 1: Replace placeholders in JSON before deserialization
//        String processedJson = replacementService.processJsonPlaceholders(json);
//        System.out.println("Processed JSON: " + processedJson);

        System.out.println("Parse Query");
        System.out.println("2 ==============================================");
        // STEP 2: Deserialize the processed JSON to Query object
        Query query = objectMapper.readValue(processedJson, Query.class);
        System.out.println(query.toString());

        System.out.println("Validate Query to ensure all required details are present");
        System.out.println("3 ==============================================");
        try {
            query.validate();
            // Proceed with processing
        } catch (IllegalArgumentException ex) {
            // Handle validation error
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        System.out.println("Generate Condition and SQL");
        System.out.println("4 ==============================================");

        // STEP 4: Generate condition and SQL for debugging using OperatorFactory directly
        Condition condition = query.toCondition(dsl, operatorFactory);
        String sql = dsl.renderInlined(dsl.select().from("your_table").where(condition));
        System.out.println("Generated SQL: " + sql);

        return condition;
    }
}
