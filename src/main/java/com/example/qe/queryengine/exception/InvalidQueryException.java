package com.example.qe.queryengine.exception;

public class InvalidQueryException extends RuntimeException {
    public InvalidQueryException(String message) {
        super(message);
    }
    public InvalidQueryException(String message, Exception e) {
        super(message, e);
    }
}
