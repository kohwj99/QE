package com.example.qe.queryengine.query;

import com.example.qe.queryengine.exception.InvalidQueryException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public final class ValueNormalizer {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private ValueNormalizer() {}

    public static Object normalize(Object rawValue, ValueType type) {
        if (rawValue == null) return null;

        try {
            return switch (type) {
                case STRING -> rawValue.toString();

                case BOOLEAN -> {
                    if (rawValue instanceof Boolean b) yield b;
                    String s = rawValue.toString().trim().toLowerCase();
                    if ("true".equals(s)) yield Boolean.TRUE;
                    if ("false".equals(s)) yield Boolean.FALSE;
                    if ("null".equals(s)) yield null;
                    throw new InvalidQueryException("Invalid boolean value: " + rawValue);
                }

                case DATE -> {
                    if (rawValue instanceof LocalDate d) yield d;

                    String s = rawValue.toString().trim();
                    // check yyyy-MM-dd format
                    if (s.length() == 10 && s.charAt(4) == '-' && s.charAt(7) == '-') {
                        yield LocalDate.parse(s, DATE_FORMATTER);
                    }
                    // fallback: if numeric, return BigDecimal
                    if (isNumber(s)) {
                        yield new BigDecimal(s);
                    }
                    throw new InvalidQueryException("Invalid DATE value: " + rawValue);
                }

                case NUMERIC -> {
                    if (rawValue instanceof BigDecimal bd) yield bd;
                    if (rawValue instanceof Number n) yield new BigDecimal(n.toString());
                    yield new BigDecimal(rawValue.toString().trim());
                }
            };
        } catch (Exception e) {
            throw new InvalidQueryException(
                    "Failed to normalize value '" + rawValue + "' for type " + type.name() + ": " + e.getMessage(),
                    e
            );
        }
    }

    private static boolean isNumber(String text) {
        if (text == null || text.isEmpty()) return false;
        NumberFormat format = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        format.parse(text, pos);
        return pos.getIndex() == text.length();
    }
}
