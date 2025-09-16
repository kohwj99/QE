package com.example.qe.queryengine.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    private JsonHelper() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static String toEscapedJsonString(JsonNode jsonNode) throws JsonProcessingException {
        return objectMapper.writeValueAsString(jsonNode);
    }

    public static JsonNode parseWrappedEscapedJsonString(String inputJsonString) throws JsonProcessingException {
        JsonNode rootNode = parseEscapedJsonString(inputJsonString);
        String innerJson = rootNode.get("json").asText();
        return objectMapper.readTree(innerJson);
    }

    public static JsonNode parseEscapedJsonString(String inputJsonString) throws JsonProcessingException {
        return objectMapper.readTree(inputJsonString);
    }
}
