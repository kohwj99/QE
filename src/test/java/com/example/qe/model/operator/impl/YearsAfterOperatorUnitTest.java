package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("YearsAfterOperator Unit Tests")
class YearsAfterOperatorUnitTest {

    private YearsAfterOperator yearsAfterOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        yearsAfterOperator = new YearsAfterOperator();
        dateField = DSL.field("expiry_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenPositiveYears_shouldReturnYearsAfterCondition")
    void apply_givenPositiveYears_shouldReturnYearsAfterCondition() {
        Integer yearsValue = 3;

        Condition condition = yearsAfterOperator.apply(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusYears(3);
        assertTrue(sql.contains("expiry_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("apply_givenZeroYears_shouldReturnTodayCondition")
    void apply_givenZeroYears_shouldReturnTodayCondition() {
        Integer yearsValue = 0;

        Condition condition = yearsAfterOperator.apply(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now();
        assertTrue(sql.contains("expiry_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
    }

    @Test
    @DisplayName("apply_givenSingleYear_shouldReturnYearAfterCondition")
    void apply_givenSingleYear_shouldReturnYearAfterCondition() {
        Integer yearsValue = 1;

        Condition condition = yearsAfterOperator.apply(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusYears(1);
        assertTrue(sql.contains("expiry_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("apply_givenLargeYears_shouldReturnDistantFutureCondition")
    void apply_givenLargeYears_shouldReturnDistantFutureCondition() {
        Integer yearsValue = 50;

        Condition condition = yearsAfterOperator.apply(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusYears(50);
        assertTrue(sql.contains("expiry_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }
}
