package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.DaysAfterOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DaysAfterOperator Tests")
class DaysAfterOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(DaysAfterOperatorTest.class);

    private DaysAfterOperator daysAfterOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        daysAfterOperator = new DaysAfterOperator();
        dateField = DSL.field("event_date", LocalDate.class);
        logger.info("Created DaysAfterOperator and test field");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith30DaysAfter_shouldReturnDaysAfterCondition")
    void apply_givenValidDateFieldWith30DaysAfter_shouldReturnDaysAfterCondition() {
        logger.info("=== Testing DaysAfterOperator with 30 days ===");

        Integer daysValue = 30;
        Condition condition = daysAfterOperator.apply(dateField, daysValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysAfter({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now().plusDays(30);

        assertAll("Date daysAfter validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("event_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ DaysAfterOperator correctly generates condition for 30 days");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith7DaysAfter_shouldReturnDaysAfterCondition")
    void apply_givenValidDateFieldWith7DaysAfter_shouldReturnDaysAfterCondition() {
        logger.info("=== Testing DaysAfterOperator with 7 days ===");

        Integer daysValue = 7;
        Condition condition = daysAfterOperator.apply(dateField, daysValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysAfter({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now().plusDays(7);

        assertAll("Date daysAfter validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("event_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ DaysAfterOperator correctly generates condition for 7 days");
    }

    @Test
    @DisplayName("apply_givenZeroDays_shouldReturnTodayCondition")
    void apply_givenZeroDays_shouldReturnTodayCondition() {
        logger.info("=== Testing DaysAfterOperator with 0 days (today) ===");

        Integer daysValue = 0;
        Condition condition = daysAfterOperator.apply(dateField, daysValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysAfter({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now();

        assertAll("Date daysAfter validation for today",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("event_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date")
        );

        logger.info("✓ DaysAfterOperator correctly generates condition for today");
    }
}
