package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.EqualsOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EqualsOperator Unit Tests")
class EqualsOperatorUnitTest {

    private EqualsOperator equalsOperator;
    private Field<String> stringField;
    private Field<Integer> integerField;

    @BeforeEach
    void setUp() {
        equalsOperator = new EqualsOperator();
        stringField = DSL.field("name", String.class);
        integerField = DSL.field("age", Integer.class);
    }

    @Test
    @DisplayName("apply_givenStringValue_shouldReturnEqualsCondition")
    void apply_givenStringValue_shouldReturnEqualsCondition() {
        String value = "test_value";

        Condition condition = equalsOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("test_value"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenIntegerValue_shouldReturnEqualsCondition")
    void apply_givenIntegerValue_shouldReturnEqualsCondition() {
        Integer value = 42;

        Condition condition = equalsOperator.apply(integerField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("age"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("42"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenNullValue_shouldReturnEqualsCondition")
    void apply_givenNullValue_shouldReturnEqualsCondition() {
        Condition condition = equalsOperator.apply(stringField, null);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnEqualsCondition")
    void apply_givenEmptyString_shouldReturnEqualsCondition() {
        String value = "";

        Condition condition = equalsOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
    }
}
