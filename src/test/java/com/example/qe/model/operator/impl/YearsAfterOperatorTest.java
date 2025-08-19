package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.YearsAfterOperator;
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

@DisplayName("YearsAfterOperator Tests")
class YearsAfterOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(YearsAfterOperatorTest.class);

    private YearsAfterOperator yearsAfterOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        yearsAfterOperator = new YearsAfterOperator();
        dateField = DSL.field("expiry_date", LocalDate.class);
        logger.info("Created YearsAfterOperator and test field");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith3YearsAfter_shouldReturnYearsAfterCondition")
    void apply_givenValidDateFieldWith3YearsAfter_shouldReturnYearsAfterCondition() {
        logger.info("=== Testing YearsAfterOperator with 3 years ===");

        Integer yearsValue = 3;
        Condition condition = yearsAfterOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsAfter({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now().plusYears(3);

        assertAll("Date yearsAfter validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("expiry_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ YearsAfterOperator correctly generates condition for 3 years");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith7YearsAfter_shouldReturnYearsAfterCondition")
    void apply_givenValidDateFieldWith7YearsAfter_shouldReturnYearsAfterCondition() {
        logger.info("=== Testing YearsAfterOperator with 7 years ===");

        Integer yearsValue = 7;
        Condition condition = yearsAfterOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsAfter({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now().plusYears(7);

        assertAll("Date yearsAfter validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("expiry_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ YearsAfterOperator correctly generates condition for 7 years");
    }

    @Test
    @DisplayName("apply_givenZeroYears_shouldReturnTodayCondition")
    void apply_givenZeroYears_shouldReturnTodayCondition() {
        logger.info("=== Testing YearsAfterOperator with 0 years (today) ===");

        Integer yearsValue = 0;
        Condition condition = yearsAfterOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsAfter({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now();

        assertAll("Date yearsAfter validation for today",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("expiry_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date")
        );

        logger.info("✓ YearsAfterOperator correctly generates condition for today");
    }
}
