package com.example.qe.queryengine.replacement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a placeholder value that can be dynamically replaced at runtime.
 * Examples: [me], [today], [now], [current_user], etc.
 */
public class Replaceable {

    private final String placeholder;
    private final String type;

    @JsonCreator
    public Replaceable(@JsonProperty("placeholder") String placeholder,
                      @JsonProperty("type") String type) {
        this.placeholder = placeholder;
        this.type = type;
    }

    /**
     * Creates a Replaceable from a placeholder string like "[me]"
     */
    public static Replaceable fromString(String value) {
        if (value == null || !isPlaceholder(value)) {
            return null;
        }

        // Extract placeholder name from [placeholder] format
        String placeholderName = value.substring(1, value.length() - 1);
        return new Replaceable(placeholderName, inferType(placeholderName));
    }

    /**
     * Checks if a string value is a placeholder (surrounded by [])
     */
    public static boolean isPlaceholder(String value) {
        return value != null &&
               value.length() > 2 &&
               value.startsWith("[") &&
               value.endsWith("]");
    }

    /**
     * Infers the type based on placeholder name
     */
    private static String inferType(String placeholderName) {
        return switch (placeholderName.toLowerCase()) {
            case "today", "now", "current_date", "yesterday", "tomorrow" -> "date";
            case "current_time", "current_timestamp" -> "timestamp";
            case "me", "current_user", "user_id" -> "user";
            case "current_year", "current_month", "current_day" -> "integer";
            default -> "string";
        };
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getType() {
        return type;
    }

    /**
     * Returns the full placeholder string with brackets
     */
    public String getFullPlaceholder() {
        return "[" + placeholder + "]";
    }

    @Override
    public String toString() {
        return "Replaceable{" +
                "placeholder='" + placeholder + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Replaceable that = (Replaceable) o;
        return placeholder.equals(that.placeholder);
    }

    @Override
    public int hashCode() {
        return placeholder.hashCode();
    }
}
