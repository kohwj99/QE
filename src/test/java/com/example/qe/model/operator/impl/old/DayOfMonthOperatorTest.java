package com.example.qe.model.operator.impl.old;

import com.example.qe.model.operator.impl.DayOfMonthOperator;
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

@DisplayName("DayOfMonth Operator Test")
class DayOfMonthOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(DayOfMonthOperatorTest.class);

    private DayOfMonthOperator dayOfMonthOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        logger.info("=== Setting up DayOfMonth Operator test ===");
        dayOfMonthOperator = new DayOfMonthOperator();
        dateField = DSL.field("created_date", LocalDate.class);
        logger.info("Created DayOfMonthOperator and test field");
    }

    @Test
    @DisplayName("DayOfMonthOperator should create correct condition for day extraction")
    void testDayOfMonthOperator() {
        logger.info("=== Testing DayOfMonthOperator ===");

        Integer dayValue = 15;
        @SuppressWarnings("unchecked")
        var condition = dayOfMonthOperator.apply(dateField, BigDecimal.valueOf(dayValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for dayOfMonth({}) : {}", dayValue, sql);

        assertTrue(sql.contains("day") || sql.contains("DAY"), "SQL should contain day extraction");
        assertTrue(sql.contains("15"), "SQL should contain the day value");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator");

        logger.info("✓ DayOfMonthOperator correctly generates day extraction condition");
    }

    @Test
    @DisplayName("DayOfMonthOperator should handle different day values")
    void testDayOfMonthOperatorWithDifferentValues() {
        logger.info("=== Testing DayOfMonthOperator with different day values ===");

        // Test with various day values
        Integer[] testDays = {1, 15, 31};

        for (Integer day : testDays) {
            @SuppressWarnings("unchecked")
            var condition = dayOfMonthOperator.apply(dateField, BigDecimal.valueOf(day));

            assertNotNull(condition, "Condition should not be null for day " + day);
            String sql = condition.toString();
            logger.info("Generated SQL for dayOfMonth({}) : {}", day, sql);

            assertTrue(sql.contains(day.toString()), "SQL should contain the day value " + day);
            assertTrue(sql.contains("day") || sql.contains("DAY"), "SQL should contain day extraction");
        }

        logger.info("✓ DayOfMonthOperator correctly handles different day values");
    }

    @Test
    @DisplayName("DayOfMonthOperator should work with different field names")
    void testDayOfMonthOperatorWithDifferentFields() {
        logger.info("=== Testing DayOfMonthOperator with different field names ===");

        Field<LocalDate> birthDateField = DSL.field("birth_date", LocalDate.class);
        Field<LocalDate> updatedAtField = DSL.field("updated_at", LocalDate.class);

        Integer dayValue = 10;

        // Test with birth_date field
        @SuppressWarnings("unchecked")
        var birthDateCondition = dayOfMonthOperator.apply(birthDateField, BigDecimal.valueOf(dayValue));
        assertNotNull(birthDateCondition);
        String birthDateSql = birthDateCondition.toString();
        logger.info("Birth date condition: {}", birthDateSql);

        // Test with updated_at field
        @SuppressWarnings("unchecked")
        var updatedAtCondition = dayOfMonthOperator.apply(updatedAtField, BigDecimal.valueOf(dayValue));
        assertNotNull(updatedAtCondition);
        String updatedAtSql = updatedAtCondition.toString();
        logger.info("Updated at condition: {}", updatedAtSql);

        // Both should contain the day value and day extraction
        assertTrue(birthDateSql.contains("10"), "Birth date SQL should contain day value");
        assertTrue(updatedAtSql.contains("10"), "Updated at SQL should contain day value");

        logger.info("✓ DayOfMonthOperator works correctly with different field names");
    }

    @Test
    @DisplayName("DayOfMonthOperator should handle edge case day values")
    void testDayOfMonthOperatorEdgeCases() {
        logger.info("=== Testing DayOfMonthOperator with edge case values ===");

        // Test edge cases
        Integer[] edgeCases = {1, 31}; // First and last possible days of month

        for (Integer day : edgeCases) {
            @SuppressWarnings("unchecked")
            var condition = dayOfMonthOperator.apply(dateField, BigDecimal.valueOf(day));

            assertNotNull(condition, "Condition should not be null for edge case day " + day);
            String sql = condition.toString();
            logger.info("Generated SQL for edge case dayOfMonth({}) : {}", day, sql);

            assertTrue(sql.contains(day.toString()), "SQL should contain the edge case day value " + day);
        }

        logger.info("✓ DayOfMonthOperator correctly handles edge case day values");
    }
}
