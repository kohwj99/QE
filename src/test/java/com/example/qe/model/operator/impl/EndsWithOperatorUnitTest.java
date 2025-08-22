package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.EndsWithOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EndsWithOperator Unit Tests")
class EndsWithOperatorUnitTest {

    private EndsWithOperator endsWithOperator;
    private Field<String> stringField;

    @BeforeEach
    void setUp() {
        endsWithOperator = new EndsWithOperator();
        stringField = DSL.field("filename", String.class);
    }

    @Test
    @DisplayName("apply_givenStringSuffix_shouldReturnEndsWithCondition")
    void apply_givenStringSuffix_shouldReturnEndsWithCondition() {
        String value = ".pdf";

        Condition condition = endsWithOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("filename"), "SQL should contain field name");
        assertTrue(sql.toLowerCase().contains("like"), "SQL should contain LIKE operator");
        assertTrue(sql.contains("%.pdf"), "SQL should contain suffix with wildcard");
    }

    @Test
    @DisplayName("apply_givenSingleCharacter_shouldReturnEndsWithCondition")
    void apply_givenSingleCharacter_shouldReturnEndsWithCondition() {
        String value = "Z";

        Condition condition = endsWithOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("filename"), "SQL should contain field name");
        assertTrue(sql.contains("%Z"), "SQL should contain single character with wildcard");
    }

    @Test
    @DisplayName("apply_givenEmptyString_shouldReturnEndsWithCondition")
    void apply_givenEmptyString_shouldReturnEndsWithCondition() {
        String value = "";

        Condition condition = endsWithOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("filename"), "SQL should contain field name");
        assertTrue(sql.contains("%"), "SQL should contain wildcard for empty suffix");
    }

    @Test
    @DisplayName("apply_givenSpecialCharacters_shouldReturnEndsWithCondition")
    void apply_givenSpecialCharacters_shouldReturnEndsWithCondition() {
        String value = "_backup.txt";

        Condition condition = endsWithOperator.apply(stringField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("filename"), "SQL should contain field name");
        assertTrue(sql.contains("%_backup.txt"), "SQL should contain suffix with special characters");
    }
}