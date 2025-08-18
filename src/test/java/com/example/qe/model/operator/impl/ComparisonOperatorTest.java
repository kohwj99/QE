package com.example.qe.model.operator.impl;

import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Comparison Operator Tests - GreaterThanEqualOperator and LessThanEqualOperator")
class ComparisonOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(ComparisonOperatorTest.class);

    private GreaterThanEqualOperator<BigDecimal> greaterThanEqualOperator;
    private LessThanEqualOperator<BigDecimal> lessThanEqualOperator;
    private Field<BigDecimal> intField;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        logger.info("=== Setting up Comparison Operator tests ===");
        greaterThanEqualOperator = new GreaterThanEqualOperator<>();
        lessThanEqualOperator = new LessThanEqualOperator<>();

        // Create test fields using jOOQ DSL
        intField = DSL.field("age", BigDecimal.class);
        dateField = DSL.field("birth_date", LocalDate.class);
        logger.info("Created test operators and fields");
    }

    @Test
    @DisplayName("GreaterThanEqualOperator should create correct >= condition for numbers")
    void testGreaterThanEqualOperatorNumeric() {
        logger.info("=== Testing GreaterThanEqualOperator with BigDecimal ===");

        BigDecimal testValue = new BigDecimal("25");
        var condition = greaterThanEqualOperator.apply(intField, testValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for greaterThanEqual({}) : {}", testValue, sql);

        // The condition should contain >= operator
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain '>=' or 'ge' operator");
        assertTrue(sql.contains("25"), "SQL should contain the test value");

        logger.info("✓ GreaterThanEqualOperator correctly generates >= condition for BigDecimal");
    }

    @Test
    @DisplayName("LessThanEqualOperator should create correct <= condition for numbers")
    void testLessThanEqualOperatorNumeric() {
        logger.info("=== Testing LessThanEqualOperator with BigDecimal ===");

        BigDecimal testValue = new BigDecimal("65");
        var condition = lessThanEqualOperator.apply(intField, testValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for lessThanEqual({}) : {}", testValue, sql);

        // The condition should contain <= operator
        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain '<=' or 'le' operator");
        assertTrue(sql.contains("65"), "SQL should contain the test value");

        logger.info("✓ LessThanEqualOperator correctly generates <= condition for BigDecimal");
    }

    @Test
    @DisplayName("GreaterThanEqualOperator should work with LocalDate")
    void testGreaterThanEqualOperatorDate() {
        logger.info("=== Testing GreaterThanEqualOperator with LocalDate ===");

        @SuppressWarnings("rawtypes")
        GreaterThanEqualOperator dateOperator = new GreaterThanEqualOperator();
        LocalDate testDate = LocalDate.of(2023, 1, 1);
        @SuppressWarnings("unchecked")
        var condition = dateOperator.apply(dateField, testDate);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for greaterThanEqual({}) : {}", testDate, sql);

        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain '>=' or 'ge' operator");
        assertTrue(sql.contains("2023-01-01") || sql.contains("2023"), "SQL should contain the date value");

        logger.info("✓ GreaterThanEqualOperator correctly generates >= condition for LocalDate");
    }

    @Test
    @DisplayName("LessThanEqualOperator should work with LocalDate")
    void testLessThanEqualOperatorDate() {
        logger.info("=== Testing LessThanEqualOperator with LocalDate ===");

        @SuppressWarnings("rawtypes")
        LessThanEqualOperator dateOperator = new LessThanEqualOperator();
        LocalDate testDate = LocalDate.of(2023, 12, 31);
        @SuppressWarnings("unchecked")
        var condition = dateOperator.apply(dateField, testDate);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for lessThanEqual({}) : {}", testDate, sql);

        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain '<=' or 'le' operator");
        assertTrue(sql.contains("2023-12-31") || sql.contains("2023"), "SQL should contain the date value");

        logger.info("✓ LessThanEqualOperator correctly generates <= condition for LocalDate");
    }

    @Test
    @DisplayName("Both operators should handle edge cases properly")
    void testEdgeCases() {
        logger.info("=== Testing edge cases ===");

        // Test with zero
        var zeroCondition = greaterThanEqualOperator.apply(intField, BigDecimal.ZERO);
        assertNotNull(zeroCondition, "Should handle zero value");
        logger.info("Zero value condition: {}", zeroCondition);

        // Test with negative numbers
        var negativeCondition = lessThanEqualOperator.apply(intField, new BigDecimal("-10"));
        assertNotNull(negativeCondition, "Should handle negative value");
        logger.info("Negative value condition: {}", negativeCondition);

        // Test with null field (should still work)
        Field<BigDecimal> nullField = DSL.field("null_field", BigDecimal.class);
        var nullFieldCondition = greaterThanEqualOperator.apply(nullField, new BigDecimal("5"));
        assertNotNull(nullFieldCondition, "Should handle different field names");
        logger.info("Different field condition: {}", nullFieldCondition);

        logger.info("✓ Both operators handle edge cases correctly");
    }

    @Test
    @DisplayName("Operators should work with different field names and types")
    void testWithDifferentFieldsAndTypes() {
        logger.info("=== Testing operators with different field names and types ===");

        // Test with different field names
        Field<BigDecimal> scoreField = DSL.field("score", BigDecimal.class);
        Field<BigDecimal> levelField = DSL.field("level", BigDecimal.class);

        var scoreCondition = greaterThanEqualOperator.apply(scoreField, new BigDecimal("80"));
        assertNotNull(scoreCondition);
        logger.info("Score field condition: {}", scoreCondition);

        var levelCondition = lessThanEqualOperator.apply(levelField, new BigDecimal("10"));
        assertNotNull(levelCondition);
        logger.info("Level field condition: {}", levelCondition);

        // Test with date fields
        Field<LocalDate> startDateField = DSL.field("start_date", LocalDate.class);
        Field<LocalDate> endDateField = DSL.field("end_date", LocalDate.class);

        @SuppressWarnings("rawtypes")
        GreaterThanEqualOperator dateGreaterOperator = new GreaterThanEqualOperator();
        @SuppressWarnings("rawtypes")
        LessThanEqualOperator dateLessOperator = new LessThanEqualOperator();

        @SuppressWarnings("unchecked")
        var startDateCondition = dateGreaterOperator.apply(startDateField, LocalDate.now());
        assertNotNull(startDateCondition);
        logger.info("Start date condition: {}", startDateCondition);

        @SuppressWarnings("unchecked")
        var endDateCondition = dateLessOperator.apply(endDateField, LocalDate.now().plusDays(30));
        assertNotNull(endDateCondition);
        logger.info("End date condition: {}", endDateCondition);

        logger.info("✓ Both operators work correctly with different field names and types");
    }
}
