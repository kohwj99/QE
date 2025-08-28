package com.example.qe.queryengine.query.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DateQueryDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        // Try LocalDate first
        try {
            return LocalDate.parse(text);
        } catch (Exception ignored) {}
        // Try BigDecimal
        try {
            return new BigDecimal(text);
        } catch (Exception ignored) {}
        // Fallback: return as String
        return text;
    }
}