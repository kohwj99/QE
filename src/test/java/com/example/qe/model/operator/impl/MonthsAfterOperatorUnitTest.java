package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.MonthsAfterOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MonthsAfterOperator Unit Tests")
class MonthsAfterOperatorUnitTest {

    private MonthsAfterOperator monthsAfterOperator;
    private Field<?> dateField;

    @BeforeEach
    void setUp() {
        monthsAfterOperator = new MonthsAfterOperator();
        dateField = DSL.field("start_date");
    }

    @Test
    @DisplayName("applyToField_givenPositiveMonths_shouldReturnMonthsAfterCondition")
    void applyToField_givenPositiveMonths_shouldReturnMonthsAfterCondition() {
        BigDecimal monthsValue = new BigDecimal("6");

        Condition condition = monthsAfterOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusMonths(6);
        assertTrue(sql.contains("start_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("applyToField_givenZeroMonths_shouldReturnTodayCondition")
    void applyToField_givenZeroMonths_shouldReturnTodayCondition() {
        BigDecimal monthsValue = new BigDecimal("0");

        Condition condition = monthsAfterOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now();
        assertTrue(sql.contains("start_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
    }

    @Test
    @DisplayName("applyToField_givenSingleMonth_shouldReturnMonthAfterCondition")
    void applyToField_givenSingleMonth_shouldReturnMonthAfterCondition() {
        BigDecimal monthsValue = new BigDecimal("1");

        Condition condition = monthsAfterOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusMonths(1);
        assertTrue(sql.contains("start_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("applyToField_givenLargeMonths_shouldReturnDistantFutureCondition")
    void applyToField_givenLargeMonths_shouldReturnDistantFutureCondition() {
        BigDecimal monthsValue = new BigDecimal("24");

        Condition condition = monthsAfterOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusMonths(24);
        assertTrue(sql.contains("start_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("applyToField_givenDecimalMonths_shouldReturnMonthsAfterCondition")
    void applyToField_givenDecimalMonths_shouldReturnMonthsAfterCondition() {
        BigDecimal monthsValue = new BigDecimal("3.0");

        Condition condition = monthsAfterOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().plusMonths(3);
        assertTrue(sql.contains("start_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }
}
