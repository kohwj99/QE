package com.example.qe.queryengine.replaceable;

import com.example.qe.queryengine.exception.QueryReplaceableException;
import com.example.qe.queryengine.query.QueryContextDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class ReplaceableResolver {

    private final ReplaceableFactory factory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReplaceableResolver(ReplaceableFactory factory) {
        this.factory = factory;
    }

    public String processJsonPlaceholders(QueryContextDto contextDto) {
        JsonNode root = contextDto.getJson();
        try {
            resolveNode(root, contextDto);
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new QueryReplaceableException("Failed to process placeholders", e);
        }
    }

    private void resolveNode(JsonNode node, QueryContextDto contextDto) {
        if (node.isObject()) {
            ObjectNode objNode = (ObjectNode) node;
            objNode.fieldNames().forEachRemaining(field -> {
                JsonNode child = objNode.get(field);
                if (child.isTextual()) {
                    String text = child.asText();
                    if (text.startsWith("[") && text.endsWith("]")) {
                        Replaceable replaceable = factory.create(text);
                        objNode.put(field, replaceable.resolve(contextDto));
                    }
                } else {
                    resolveNode(child, contextDto);
                }
            });
        } else if (node.isArray()) {
            for (JsonNode child : node) {
                resolveNode(child, contextDto);
            }
        }
    }
}
