package com.example.qe.queryengine.exception;

public class FieldTypeMismatchException extends RuntimeException {
    public FieldTypeMismatchException(String field, String expectedType, String actualType) {
        super("Field '" + field + "' type mismatch: expected " + expectedType + ", got " + actualType);
    }
}
