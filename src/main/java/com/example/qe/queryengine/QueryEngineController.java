package com.example.qe.queryengine;

import com.example.qe.queryengine.query.QueryContextDto;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/query")
@Controller
public class QueryEngineController {

    @Autowired
    private QueryEngineService queryEngineService;

    @PostMapping("/executeQuery")
    public void executeQuery(@RequestBody QueryContextDto context) {
        try {
            // Execute the query on the actual database
            Result<Record> result = queryEngineService.executeQuery(context);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping("/executeQueryToDisplay")
    public ResponseEntity<List<Map<String, Object>>> executeQueryToDisplay(@RequestBody QueryContextDto context) {
        try {

            // Execute the query on the actual database
            List<Map<String, Object>> result = queryEngineService.executeQueryToDisplay(context);

            // Convert result to JSON format
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }
}
