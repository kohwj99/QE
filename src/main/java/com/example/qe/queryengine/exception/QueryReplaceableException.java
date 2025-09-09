package com.example.qe.queryengine.exception;

public class QueryReplaceableException extends RuntimeException {

    public QueryReplaceableException(String message, Throwable cause) {
        super(message, cause);
    }
    public QueryReplaceableException(String message) {
        super(message);
    }
}
