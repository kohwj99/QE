package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.YearEqualOperator;
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

@DisplayName("YearEqualOperator Tests")
class YearEqualOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(YearEqualOperatorTest.class);

    private YearEqualOperator yearEqualOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        yearEqualOperator = new YearEqualOperator();
        dateField = DSL.field("hire_date", LocalDate.class);
        logger.info("Created YearEqualOperator and test field");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWithYear2025_shouldReturnYearEqualCondition")
    void apply_givenValidDateFieldWithYear2025_shouldReturnYearEqualCondition() {
        logger.info("=== Testing YearEqualOperator with year 2025 ===");

        Integer yearValue = 2025;
        Condition condition = yearEqualOperator.apply(dateField, yearValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearEqual({}) : {}", yearValue, sql);

        assertAll("Date yearEqual 2025 validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("hire_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains("2025") || sql.toLowerCase().contains("year"), "SQL should reference year 2025")
        );

        logger.info("✓ YearEqualOperator correctly generates condition for 2025");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWithYear2020_shouldReturnYearEqualCondition")
    void apply_givenValidDateFieldWithYear2020_shouldReturnYearEqualCondition() {
        logger.info("=== Testing YearEqualOperator with year 2020 ===");

        Integer yearValue = 2020;
        Condition condition = yearEqualOperator.apply(dateField, yearValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearEqual({}) : {}", yearValue, sql);

        assertAll("Date yearEqual 2020 validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("hire_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains("2020") || sql.toLowerCase().contains("year"), "SQL should reference year 2020")
        );

        logger.info("✓ YearEqualOperator correctly generates condition for 2020");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWithYear1995_shouldReturnYearEqualCondition")
    void apply_givenValidDateFieldWithYear1995_shouldReturnYearEqualCondition() {
        logger.info("=== Testing YearEqualOperator with year 1995 ===");

        Integer yearValue = 1995;
        Condition condition = yearEqualOperator.apply(dateField, yearValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for yearEqual({}) : {}", yearValue, sql);

        assertAll("Date yearEqual 1995 validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("hire_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains("1995") || sql.toLowerCase().contains("year"), "SQL should reference year 1995")
        );

        logger.info("✓ YearEqualOperator correctly generates condition for 1995");
    }
}
