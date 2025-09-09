package com.example.qe.queryengine.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts a JsonNode to a JSON string in "escaped" format for DB storage.
     * Example output:
     * {"json":"{\"type\":\"AndQuery\",\"children\":[...]}"}
     */
    public static String toEscapedJsonString(JsonNode jsonNode) throws JsonProcessingException {
        // Create an outer object with a single "json" field
        ObjectNode outerNode = objectMapper.createObjectNode();

        // Convert the inner JsonNode to string and store it in the "json" field
        String innerJsonString = objectMapper.writeValueAsString(jsonNode);
        outerNode.put("json", innerJsonString);

        // Serialize the outer node to string for DB storage
        return objectMapper.writeValueAsString(outerNode);
    }

    public static JsonNode parseEscapedJsonString(String inputJsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Step 1: parse the outer JSON
        JsonNode rootNode = objectMapper.readTree(inputJsonString);

        // Step 2: extract inner JSON string and parse it
        String innerJson = rootNode.get("json").asText();
        return objectMapper.readTree(innerJson);
    }
}
