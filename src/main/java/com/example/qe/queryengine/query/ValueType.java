package com.example.qe.queryengine.query;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public enum ValueType {
    STRING(String.class),
    BOOLEAN(Boolean.class),
    DATE(LocalDate.class),
    NUMERIC(BigDecimal.class);

    private final Class<?> clazz;

    ValueType(Class<?> clazz) {
        this.clazz = clazz;
    }

}