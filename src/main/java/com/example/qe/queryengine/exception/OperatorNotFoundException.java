package com.example.qe.queryengine.exception;

public class OperatorNotFoundException extends RuntimeException {
    public OperatorNotFoundException(String operator) {
        super("Operator not found: " + operator);
    }
}