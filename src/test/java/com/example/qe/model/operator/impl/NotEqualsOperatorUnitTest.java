package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.NotEqualsOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NotEqualsOperator Unit Tests")
class NotEqualsOperatorUnitTest {

    private NotEqualsOperator notEqualsOperator;
    private Field<String> stringField;
    private Field<Integer> integerField;

    @BeforeEach
    void setUp() {
        notEqualsOperator = new NotEqualsOperator();
        stringField = DSL.field("status", String.class);
        integerField = DSL.field("count", Integer.class);
    }

    @Test
    @DisplayName("apply_givenStringValue_shouldReturnNotEqualsCondition")
    void apply_givenStringValue_shouldReturnNotEqualsCondition() {
        String value = "deleted";
        
        Condition condition = notEqualsOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("status"), "SQL should contain field name");
        assertTrue(sql.contains("<>") || sql.contains("!=") || sql.contains("ne"), "SQL should contain not equals operator");
        assertTrue(sql.contains("deleted"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenIntegerValue_shouldReturnNotEqualsCondition")
    void apply_givenIntegerValue_shouldReturnNotEqualsCondition() {
        Integer value = 0;
        
        Condition condition = notEqualsOperator.apply(integerField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("count"), "SQL should contain field name");
        assertTrue(sql.contains("<>") || sql.contains("!=") || sql.contains("ne"), "SQL should contain not equals operator");
        assertTrue(sql.contains("0"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenNullValue_shouldReturnNotEqualsCondition")
    void apply_givenNullValue_shouldReturnNotEqualsCondition() {
        Condition condition = notEqualsOperator.apply(stringField, null);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("status"), "SQL should contain field name");
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnNotEqualsCondition")
    void apply_givenEmptyString_shouldReturnNotEqualsCondition() {
        String value = "";
        
        Condition condition = notEqualsOperator.apply(stringField, value);
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("status"), "SQL should contain field name");
        assertTrue(sql.contains("<>") || sql.contains("!=") || sql.contains("ne"), "SQL should contain not equals operator");
    }
}
