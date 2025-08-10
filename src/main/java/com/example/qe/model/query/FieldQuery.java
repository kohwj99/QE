package com.example.qe.model.query;

public abstract class FieldQuery<T> implements Query {

    private String column;
    private String operator;
    private T value;
}
