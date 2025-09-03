package com.example.qe.queryengine.query.deserializer;

import com.example.qe.queryengine.exception.QueryDeserializationException;
import com.example.qe.queryengine.exception.QueryEngineException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BooleanQueryDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize (JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();

        if (text == null || text.equalsIgnoreCase("null")) {
            return null; // treat null, "null", or "" as Java null
        }

        if (text.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }

        if (text.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }

        throw new QueryDeserializationException("Boolean value can only be true, false, null, or empty string");
    }
}