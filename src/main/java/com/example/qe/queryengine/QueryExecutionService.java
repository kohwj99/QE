//package com.example.qe.queryengine;
//
//import com.example.qe.queryengine.query.Query;
//import com.example.qe.queryengine.replacement.ReplacementService;
//import com.example.qe.queryengine.operator.OperatorFactory;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.jooq.Condition;
//import org.jooq.DSLContext;
//import org.jooq.Record;
//import org.jooq.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class QueryExecutionService {
//
//    private final OperatorFactory operatorFactory;
//    private final ObjectMapper objectMapper;
//    private final DSLContext dsl;
//    private final ReplacementService replacementService;
//
//    @Autowired
//    public QueryExecutionService(OperatorFactory operatorFactory,
//                                DSLContext dsl,
//                                ReplacementService replacementService) {
//        this.operatorFactory = operatorFactory;
//        this.replacementService = replacementService;
//        this.dsl = dsl;
//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.registerModule(new JavaTimeModule());
//    }
//
//    public Condition parseJsonToCondition(String json) throws JsonProcessingException {
//        System.out.println("Original JSON: " + json);
//        if (json == null || json.trim().isEmpty()) {
//            throw new IllegalArgumentException("JSON cannot be null or empty");
//        }
//
//        // STEP 1: Replace placeholders in JSON before deserialization
//        String processedJson = replacementService.processJsonPlaceholders(json);
//        System.out.println("Processed JSON: " + processedJson);
//
//        // STEP 2: Deserialize the processed JSON to Query object
//        Query query = objectMapper.readValue(processedJson, Query.class);
//
//        // STEP 3: Generate condition and SQL for debugging using OperatorFactory directly
//        Condition condition = query.toCondition(dsl, operatorFactory);
//        String sql = dsl.renderInlined(dsl.select().from("your_table").where(condition));
//        System.out.println("Generated SQL: " + sql);
//
//        return condition;
//    }
//
//    public Result<Record> executeQuery(String tableName, Condition condition) {
//        return dsl.select()
//                  .from(tableName)
//                  .where(condition)
//                  .fetch();
//    }
//}
