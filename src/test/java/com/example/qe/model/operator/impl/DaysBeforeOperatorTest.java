package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.DaysBeforeOperator;
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

@DisplayName("DaysBeforeOperator Tests")
class DaysBeforeOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(DaysBeforeOperatorTest.class);

    private DaysBeforeOperator daysBeforeOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        daysBeforeOperator = new DaysBeforeOperator();
        dateField = DSL.field("reminder_date", LocalDate.class);
        logger.info("Created DaysBeforeOperator and test field");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith30DaysBefore_shouldReturnDaysBeforeCondition")
    void apply_givenValidDateFieldWith30DaysBefore_shouldReturnDaysBeforeCondition() {
        logger.info("=== Testing DaysBeforeOperator with 30 days ===");

        Integer daysValue = 30;
        Condition condition = daysBeforeOperator.apply(dateField, daysValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysBefore({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now().minusDays(30);

        assertAll("Date daysBefore validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("reminder_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ DaysBeforeOperator correctly generates condition for 30 days");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith7DaysBefore_shouldReturnDaysBeforeCondition")
    void apply_givenValidDateFieldWith7DaysBefore_shouldReturnDaysBeforeCondition() {
        logger.info("=== Testing DaysBeforeOperator with 7 days ===");

        Integer daysValue = 7;
        Condition condition = daysBeforeOperator.apply(dateField, daysValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysBefore({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now().minusDays(7);

        assertAll("Date daysBefore validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("reminder_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ DaysBeforeOperator correctly generates condition for 7 days");
    }

    @Test
    @DisplayName("apply_givenZeroDays_shouldReturnTodayCondition")
    void apply_givenZeroDays_shouldReturnTodayCondition() {
        logger.info("=== Testing DaysBeforeOperator with 0 days (today) ===");

        Integer daysValue = 0;
        Condition condition = daysBeforeOperator.apply(dateField, daysValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for daysBefore({}) : {}", daysValue, sql);

        LocalDate expectedDate = LocalDate.now();

        assertAll("Date daysBefore validation for today",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("reminder_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date")
        );

        logger.info("✓ DaysBeforeOperator correctly generates condition for today");
    }
}
