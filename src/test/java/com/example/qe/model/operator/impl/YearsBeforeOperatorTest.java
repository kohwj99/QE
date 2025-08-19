package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.YearsBeforeOperator;
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

@DisplayName("YearsBeforeOperator Tests")
class YearsBeforeOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(YearsBeforeOperatorTest.class);

    private YearsBeforeOperator yearsBeforeOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        yearsBeforeOperator = new YearsBeforeOperator();
        dateField = DSL.field("contract_date", LocalDate.class);
        logger.info("Created YearsBeforeOperator and test field");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith5YearsBefore_shouldReturnYearsBeforeCondition")
    void apply_givenValidDateFieldWith5YearsBefore_shouldReturnYearsBeforeCondition() {
        logger.info("=== Testing YearsBeforeOperator with 5 years ===");

        Integer yearsValue = 5;
        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsBefore({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now().minusYears(5);

        assertAll("Date yearsBefore validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("contract_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ YearsBeforeOperator correctly generates condition for 5 years");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWith10YearsBefore_shouldReturnYearsBeforeCondition")
    void apply_givenValidDateFieldWith10YearsBefore_shouldReturnYearsBeforeCondition() {
        logger.info("=== Testing YearsBeforeOperator with 10 years ===");

        Integer yearsValue = 10;
        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsBefore({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now().minusYears(10);

        assertAll("Date yearsBefore validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("contract_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date")
        );

        logger.info("✓ YearsBeforeOperator correctly generates condition for 10 years");
    }

    @Test
    @DisplayName("apply_givenZeroYears_shouldReturnTodayCondition")
    void apply_givenZeroYears_shouldReturnTodayCondition() {
        logger.info("=== Testing YearsBeforeOperator with 0 years (today) ===");

        Integer yearsValue = 0;
        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearsBefore({}) : {}", yearsValue, sql);

        LocalDate expectedDate = LocalDate.now();

        assertAll("Date yearsBefore validation for today",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("contract_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date")
        );

        logger.info("✓ YearsBeforeOperator correctly generates condition for today");
    }
}
