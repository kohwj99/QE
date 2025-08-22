package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IsNotNullOperator Unit Tests")
class IsNotNullOperatorUnitTest {

    private IsNotNullOperator isNotNullOperator;
    private Field<String> stringField;
    private Field<Integer> integerField;

    @BeforeEach
    void setUp() {
        isNotNullOperator = new IsNotNullOperator();
        stringField = DSL.field("email", String.class);
        integerField = DSL.field("user_id", Integer.class);
    }

    @Test
    @DisplayName("apply_givenStringField_shouldReturnIsNotNullCondition")
    void apply_givenStringField_shouldReturnIsNotNullCondition() {
        // Value parameter is ignored for IS NOT NULL operations
        Object value = null;
        
        Condition condition = isNotNullOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("email"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("is not null") || sql.toLowerCase().contains("isnotnull"), 
                   "SQL should contain IS NOT NULL operator");
    }

    @Test
    @DisplayName("apply_givenIntegerField_shouldReturnIsNotNullCondition")
    void apply_givenIntegerField_shouldReturnIsNotNullCondition() {
        Object value = null;
        
        Condition condition = isNotNullOperator.apply(integerField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("user_id"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("is not null") || sql.toLowerCase().contains("isnotnull"), 
                   "SQL should contain IS NOT NULL operator");
    }

    @Test
    @DisplayName("apply_givenIgnoredValue_shouldReturnIsNotNullCondition")
    void apply_givenIgnoredValue_shouldReturnIsNotNullCondition() {
        // IS NOT NULL operations don't use the value parameter
        String ignoredValue = "this_should_be_ignored";
        
        Condition condition = isNotNullOperator.apply(stringField, ignoredValue);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("email"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("is not null") || sql.toLowerCase().contains("isnotnull"), 
                   "SQL should contain IS NOT NULL operator");
        assertFalse(sql.contains("this_should_be_ignored"), 
                    "SQL should not contain the ignored value parameter");
    }

    @Test
    @DisplayName("apply_givenDifferentFieldName_shouldReturnIsNotNullCondition")
    void apply_givenDifferentFieldName_shouldReturnIsNotNullCondition() {
        Field<String> customField = DSL.field("custom_field", String.class);
        
        Condition condition = isNotNullOperator.apply(customField, null);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("custom_field"), "SQL should contain custom field name");
    }
}
