package com.example.qe.queryengine.exception;

public class QueryEngineException extends RuntimeException {
    public QueryEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryEngineException(String message) {
        super(message);
    }
}
