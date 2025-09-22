package com.example.qe.queryengine;

import com.example.qe.queryengine.operator.ConditionParser;
import com.example.qe.queryengine.query.QueryContextDto;
import com.example.qe.queryengine.replaceable.ReplaceableResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryEngineService {

    private final DSLContext dsl;

    @Autowired
    ConditionParser conditionParser;

    @Autowired
    ReplaceableResolver replaceableResolver;

    public QueryEngineService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Result<Record> executeQuery(QueryContextDto context) {
        String replacedJson = replaceableResolver.processJsonPlaceholders(context);
        Condition condition = conditionParser.parseJsonToCondition(replacedJson);

//        System.out.println(dsl.renderInlined(dsl.select().from(context.getTableName()).where(condition)));
        return dsl.select()
                .from(context.getTableName())
                .where(condition)
                .fetch();
    }

    public List<Map<String, Object>> executeQueryToDisplay(QueryContextDto context)  {
        String replacedJson = replaceableResolver.processJsonPlaceholders(context);
        Condition condition = conditionParser.parseJsonToCondition(replacedJson);

//        System.out.println(dsl.renderInlined(dsl.select().from(context.getTableName()).where(condition)));
        Result<Record> result = dsl.select()
                .from(context.getTableName())
                .where(condition)
                .fetch();

        return result.intoMaps();
    }

//    public List<Map<String, Object>> testStringJsonInput(String jsonInput) throws JsonProcessingException {
//        // parse the escaped JSON string
//        JsonNode jNode = parseEscapedJsonString(jsonInput);
//
//        QueryContextDto context = QueryContextDto.builder()
//                .json(jNode)
//                .tableName("TestDataTypes")
//                .createdBy("Alice")
//                .spoofDate("1999-05-14")
//                .build();
//
//        // process replaceables and parse to condition
//        String replacedJson = replaceableResolver.processJsonPlaceholders(context);
//        Condition condition = conditionParser.parseJsonToCondition(replacedJson);
//
//        Result<Record> result = dsl.select()
//                .from(context.getTableName())
//                .where(condition)
//                .fetch();
//
//        return result.intoMaps();
//    }

    public static JsonNode parseEscapedJsonString(String inputJsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Step 1: parse the outer JSON
        JsonNode rootNode = objectMapper.readTree(inputJsonString);

        // Step 2: extract inner JSON string and parse it
        String innerJson = rootNode.get("json").asText();
        return objectMapper.readTree(innerJson);
    }

}
