package com.example.qe.controller;

import com.example.qe.service.QueryExecutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class QueryExecutionController {

    @Autowired
    private QueryExecutionService queryExecutionService;

    public ResponseEntity<Condition> executeQuery(String json) throws JsonProcessingException {
        try {
            return ResponseEntity.ok(queryExecutionService.parseJsonToCondition(json));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
