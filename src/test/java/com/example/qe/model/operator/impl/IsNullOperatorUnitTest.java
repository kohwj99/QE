package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IsNullOperator Unit Tests")
class IsNullOperatorUnitTest {

    private IsNullOperator isNullOperator;
    private Field<String> stringField;
    private Field<Integer> integerField;

    @BeforeEach
    void setUp() {
        isNullOperator = new IsNullOperator();
        stringField = DSL.field("description", String.class);
        integerField = DSL.field("count", Integer.class);
    }

    @Test
    @DisplayName("apply_givenStringField_shouldReturnIsNullCondition")
    void apply_givenStringField_shouldReturnIsNullCondition() {
        // Value parameter is ignored for IS NULL operations
        Object value = null;
        
        Condition condition = isNullOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("is null") || sql.toLowerCase().contains("isnull"), 
                   "SQL should contain IS NULL operator");
    }

    @Test
    @DisplayName("apply_givenIntegerField_shouldReturnIsNullCondition")
    void apply_givenIntegerField_shouldReturnIsNullCondition() {
        Object value = null;
        
        Condition condition = isNullOperator.apply(integerField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("count"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("is null") || sql.toLowerCase().contains("isnull"), 
                   "SQL should contain IS NULL operator");
    }

    @Test
    @DisplayName("apply_givenIgnoredValue_shouldReturnIsNullCondition")
    void apply_givenIgnoredValue_shouldReturnIsNullCondition() {
        // IS NULL operations don't use the value parameter
        String ignoredValue = "this_should_be_ignored";
        
        Condition condition = isNullOperator.apply(stringField, ignoredValue);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("description"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("is null") || sql.toLowerCase().contains("isnull"), 
                   "SQL should contain IS NULL operator");
        assertFalse(sql.contains("this_should_be_ignored"), 
                    "SQL should not contain the ignored value parameter");
    }

    @Test
    @DisplayName("apply_givenDifferentFieldName_shouldReturnIsNullCondition")
    void apply_givenDifferentFieldName_shouldReturnIsNullCondition() {
        Field<String> customField = DSL.field("custom_column", String.class);
        
        Condition condition = isNullOperator.apply(customField, null);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("custom_column"), "SQL should contain custom field name");
    }
}
