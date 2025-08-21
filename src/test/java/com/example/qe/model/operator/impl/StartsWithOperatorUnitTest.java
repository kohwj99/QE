package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StartsWithOperator Unit Tests")
class StartsWithOperatorUnitTest {

    private StartsWithOperator startsWithOperator;
    private Field<String> stringField;

    @BeforeEach
    void setUp() {
        startsWithOperator = new StartsWithOperator();
        stringField = DSL.field("name", String.class);
    }

    @Test
    @DisplayName("apply_givenStringPrefix_shouldReturnStartsWithCondition")
    void apply_givenStringPrefix_shouldReturnStartsWithCondition() {
        String value = "John";
        
        Condition condition = startsWithOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
        assertTrue(sql.contains("John%"), "SQL should contain prefix with wildcard");
    }

    @Test
    @DisplayName("apply_givenSingleCharacter_shouldReturnStartsWithCondition")
    void apply_givenSingleCharacter_shouldReturnStartsWithCondition() {
        String value = "A";
        
        Condition condition = startsWithOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
        assertTrue(sql.contains("A%"), "SQL should contain single character with wildcard");
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnStartsWithCondition")
    void apply_givenEmptyString_shouldReturnStartsWithCondition() {
        String value = "";
        
        Condition condition = startsWithOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
        assertTrue(sql.contains("%"), "SQL should contain wildcard for empty prefix");
    }

    @Test
    @DisplayName("apply_givenSpecialCharacters_shouldReturnStartsWithCondition")
    void apply_givenSpecialCharacters_shouldReturnStartsWithCondition() {
        String value = "test_123";
        
        Condition condition = startsWithOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("name"), "SQL should contain field name");
        assertTrue(sql.contains("test_123%"), "SQL should contain prefix with special characters");
    }
}
