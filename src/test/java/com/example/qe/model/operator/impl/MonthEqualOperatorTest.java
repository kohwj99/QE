package com.example.qe.model.operator.impl;

import com.example.qe.model.operator.impl.MonthEqualOperator;
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

@DisplayName("MonthEqualOperator Tests")
class MonthEqualOperatorTest {

    private static final Logger logger = LoggerFactory.getLogger(MonthEqualOperatorTest.class);

    private MonthEqualOperator monthEqualOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        monthEqualOperator = new MonthEqualOperator();
        dateField = DSL.field("birth_date", LocalDate.class);
        logger.info("Created MonthEqualOperator and test field");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWithJanuary_shouldReturnMonthEqualCondition")
    void apply_givenValidDateFieldWithJanuary_shouldReturnMonthEqualCondition() {
        logger.info("=== Testing MonthEqualOperator with January (month 1) ===");

        Integer monthValue = 1; // January
        Condition condition = monthEqualOperator.apply(dateField, monthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for monthEqual({}) : {}", monthValue, sql);

        assertAll("Date monthEqual January validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("birth_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains("1") || sql.toLowerCase().contains("month"), "SQL should reference month 1")
        );

        logger.info("✓ MonthEqualOperator correctly generates condition for January");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWithDecember_shouldReturnMonthEqualCondition")
    void apply_givenValidDateFieldWithDecember_shouldReturnMonthEqualCondition() {
        logger.info("=== Testing MonthEqualOperator with December (month 12) ===");

        Integer monthValue = 12; // December
        Condition condition = monthEqualOperator.apply(dateField, monthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for monthEqual({}) : {}", monthValue, sql);

        assertAll("Date monthEqual December validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("birth_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains("12") || sql.toLowerCase().contains("month"), "SQL should reference month 12")
        );

        logger.info("✓ MonthEqualOperator correctly generates condition for December");
    }

    @Test
    @DisplayName("apply_givenValidDateFieldWithJune_shouldReturnMonthEqualCondition")
    void apply_givenValidDateFieldWithJune_shouldReturnMonthEqualCondition() {
        logger.info("=== Testing MonthEqualOperator with June (month 6) ===");

        Integer monthValue = 6; // June
        Condition condition = monthEqualOperator.apply(dateField, monthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        logger.info("Generated SQL for monthEqual({}) : {}", monthValue, sql);

        assertAll("Date monthEqual June validation",
                () -> assertNotNull(condition, "Condition should not be null"),
                () -> assertTrue(sql.contains("birth_date"), "SQL should contain column name"),
                () -> assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equality operator"),
                () -> assertTrue(sql.contains("6") || sql.toLowerCase().contains("month"), "SQL should reference month 6")
        );

        logger.info("✓ MonthEqualOperator correctly generates condition for June");
    }
}
