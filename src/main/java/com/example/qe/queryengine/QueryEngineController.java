package com.example.qe.queryengine;

import com.example.qe.queryengine.helper.JsonHelper;
import com.example.qe.queryengine.query.QueryContextDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/query")
@RestController
public class QueryEngineController {

    @Autowired
    private QueryEngineService queryEngineService;

    @PostMapping("/executeQuery")
    public void executeQuery(@RequestBody QueryContextDto context) {
        Result<Record> result = queryEngineService.executeQuery(context);

    }

    @PostMapping("/executeQueryToDisplay")
    public ResponseEntity<List<Map<String, Object>>> executeQueryToDisplay(@RequestBody QueryContextDto context) {

        // Execute the query on the actual database
        List<Map<String, Object>> result = queryEngineService.executeQueryToDisplay(context);

        // Convert result to JSON format
        return ResponseEntity.ok(result);
    }

    @PostMapping("/testJsonStringInput")
    public ResponseEntity<List<Map<String, Object>>> testJsonStringInput(@RequestBody String json) throws JsonProcessingException {

        JsonNode jsonNode = JsonHelper.parseEscapedJsonString(json);
        QueryContextDto context = QueryContextDto.builder().tableName("TestDataTypes").json(jsonNode).createdBy("Alice").spoofDate("1999-05-14").build();
        List<Map<String, Object>> result = queryEngineService.executeQueryToDisplay(context);
        return ResponseEntity.ok(result);
    }
}
