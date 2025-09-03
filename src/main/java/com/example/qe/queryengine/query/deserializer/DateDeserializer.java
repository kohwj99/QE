package com.example.qe.queryengine.query.deserializer;

import com.example.qe.queryengine.exception.QueryDeserializationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateDeserializer extends JsonDeserializer<Object> {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static boolean isNumber(String text) {
        if (text == null || text.isEmpty()) return false;
        NumberFormat format = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        format.parse(text, pos);
        return pos.getIndex() == text.length();
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text != null && text.length() == 10 && text.charAt(4) == '-' && text.charAt(7) == '-') {
            return LocalDate.parse(text, DATE_FORMATTER);
        } else if (isNumber(text)) {
            return new BigDecimal(text);
        } else {
            throw new QueryDeserializationException("Date Query can only take in valid LocalDate string or Number in some cases");
        }
    }
}
