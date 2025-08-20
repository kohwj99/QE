package com.example.qe.controller;

import com.example.qe.service.QueryExecutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/query")
@Controller
public class QueryExecutionController {

    @Autowired
    private QueryExecutionService queryExecutionService;

    @PostMapping("/executeQuery")
    public ResponseEntity<String> executeQuery(@RequestBody String json) throws JsonProcessingException {
        try {
            Condition jooq = queryExecutionService.parseJsonToCondition(json);

            return ResponseEntity.ok("works");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
