package com.example.qe.util;

public record QueryTestCase(String operator, String queryType, String column, String value, String valueType) {

    @Override
    public String toString() {
        return "OperatorTestCase{" +
                "operator='" + operator + '\'' +
                ", queryType='" + queryType + '\'' +
                ", column='" + column + '\'' +
                ", value='" + value + '\'' +
                ", valueType='" + valueType + '\'' +
                '}';
    }
}
