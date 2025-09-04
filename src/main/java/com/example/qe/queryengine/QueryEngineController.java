package com.example.qe.queryengine;

import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/query")
@Controller
public class QueryEngineController {

    @Autowired
    private QueryEngineService queryEngineService;

    @PostMapping("/executeQueryOnTable")
    public ResponseEntity<Result<Record>> executeQueryOnTable(@RequestParam String tableName, @RequestBody  String json) {
        try {

            // Execute the query on the actual database
            Result<Record> result = queryEngineService.executeQuery(tableName, json);

            // Convert result to JSON format
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
