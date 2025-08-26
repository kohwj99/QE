package com.example.qe.model.operator.impl.old;

import com.example.qe.queryengine.operator.impl.EndsWithOperator;
import com.example.qe.queryengine.operator.impl.StartsWithOperator;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("String Operator Tests - StartsWithOperator and EndsWithOperator")
class StringOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(StringOperatorTest.class);

    private StartsWithOperator startsWithOperator;
    private EndsWithOperator endsWithOperator;
    private Field<String> testField;

    @BeforeEach
    void setUp() {
        logger.info("=== Setting up String Operator tests ===");
        startsWithOperator = new StartsWithOperator();
        endsWithOperator = new EndsWithOperator();

        // Create a test field using jOOQ DSL
        testField = DSL.field("test_column", String.class);
        logger.info("Created test operators and field");
    }

    @Test
    @DisplayName("StartsWithOperator should create correct LIKE condition")
    void testStartsWithOperator() {
        logger.info("=== Testing StartsWithOperator ===");

        // Test with regular string
        String testValue = "Hello";
        var condition = startsWithOperator.apply(testField, testValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for startsWith('{}') : {}", testValue, sql);

        // The condition should contain the LIKE pattern with % at the end
        assertTrue(sql.contains("like"), "SQL should contain 'like' operator");
        assertTrue(sql.contains("Hello%"), "SQL should contain 'Hello%' pattern");

        logger.info("✓ StartsWithOperator correctly generates LIKE condition");
    }

    @Test
    @DisplayName("EndsWithOperator should create correct LIKE condition")
    void testEndsWithOperator() {
        logger.info("=== Testing EndsWithOperator ===");

        // Test with regular string
        String testValue = "World";
        var condition = endsWithOperator.apply(testField, testValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for endsWith('{}') : {}", testValue, sql);

        // The condition should contain the LIKE pattern with % at the beginning
        assertTrue(sql.contains("like"), "SQL should contain 'like' operator");
        assertTrue(sql.contains("%World"), "SQL should contain '%World' pattern");

        logger.info("✓ EndsWithOperator correctly generates LIKE condition");
    }

    @Test
    @DisplayName("StartsWithOperator should handle empty strings")
    void testStartsWithEmptyString() {
        logger.info("=== Testing StartsWithOperator with empty string ===");

        String emptyValue = "";
        var condition = startsWithOperator.apply(testField, emptyValue);

        assertNotNull(condition, "Condition should not be null for empty string");
        String sql = condition.toString();
        logger.info("Generated SQL for startsWith('') : {}", sql);

        // Should create a pattern that matches anything (just %)
        assertTrue(sql.contains("%"), "SQL should contain '%' pattern");

        logger.info("✓ StartsWithOperator handles empty string correctly");
    }

    @Test
    @DisplayName("EndsWithOperator should handle empty strings")
    void testEndsWithEmptyString() {
        logger.info("=== Testing EndsWithOperator with empty string ===");

        String emptyValue = "";
        var condition = endsWithOperator.apply(testField, emptyValue);

        assertNotNull(condition, "Condition should not be null for empty string");
        String sql = condition.toString();
        logger.info("Generated SQL for endsWith('') : {}", sql);

        // Should create a pattern that matches anything (just %)
        assertTrue(sql.contains("%"), "SQL should contain '%' pattern");

        logger.info("✓ EndsWithOperator handles empty string correctly");
    }

    @Test
    @DisplayName("StartsWithOperator should handle special characters")
    void testStartsWithSpecialCharacters() {
        logger.info("=== Testing StartsWithOperator with special characters ===");

        String specialValue = "test@domain";
        var condition = startsWithOperator.apply(testField, specialValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for startsWith('{}') : {}", specialValue, sql);

        assertTrue(sql.contains("test@domain%"), "SQL should contain the special characters in pattern");

        logger.info("✓ StartsWithOperator handles special characters correctly");
    }

    @Test
    @DisplayName("EndsWithOperator should handle special characters")
    void testEndsWithSpecialCharacters() {
        logger.info("=== Testing EndsWithOperator with special characters ===");

        String specialValue = ".com";
        var condition = endsWithOperator.apply(testField, specialValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for endsWith('{}') : {}", specialValue, sql);

        assertTrue(sql.contains("%.com"), "SQL should contain the special characters in pattern");

        logger.info("✓ EndsWithOperator handles special characters correctly");
    }

    @Test
    @DisplayName("Both operators should work with different field names")
    void testWithDifferentFields() {
        logger.info("=== Testing operators with different field names ===");

        Field<String> nameField = DSL.field("name", String.class);
        Field<String> emailField = DSL.field("email", String.class);

        // Test StartsWith with different field
        var nameCondition = startsWithOperator.apply(nameField, "John");
        assertNotNull(nameCondition);
        logger.info("StartsWith with name field: {}", nameCondition);

        // Test EndsWith with different field
        var emailCondition = endsWithOperator.apply(emailField, "@gmail.com");
        assertNotNull(emailCondition);
        logger.info("EndsWith with email field: {}", emailCondition);

        logger.info("✓ Both operators work correctly with different field names");
    }
}
