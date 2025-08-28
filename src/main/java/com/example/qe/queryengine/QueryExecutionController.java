//package com.example.qe.queryengine;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.jooq.Condition;
//import org.jooq.Record;
//import org.jooq.Result;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@RequestMapping("/api/query")
//@Controller
//public class QueryExecutionController {
//
//    @Autowired
//    private QueryExecutionService queryExecutionService;
//
//    @PostMapping("/executeQuery")
//    public ResponseEntity<String> executeQuery(@RequestBody String json) throws JsonProcessingException {
//        try {
//            Condition jooq = queryExecutionService.parseJsonToCondition(json);
//
//            return ResponseEntity.ok("Query parsed successfully. Use /executeQueryOnTable to execute on database.");
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @PostMapping("/executeQueryOnTable")
//    public ResponseEntity<?> executeQueryOnTable(@RequestBody  String json) {
//        try {
//
//            // Parse the JSON to condition
//            Condition condition = queryExecutionService.parseJsonToCondition(json);
//
//            // Execute the query on the actual database
//            Result<Record> result = queryExecutionService.executeQuery("TestDataTypes", condition);
//
//            // Convert result to JSON format
//            return ResponseEntity.ok(result.formatJSON());
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error executing query: " + e.getMessage());
//        }
//    }
//}
