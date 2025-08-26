package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.MonthsBeforeOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MonthsBeforeOperator Unit Tests")
class MonthsBeforeOperatorUnitTest {

    private MonthsBeforeOperator monthsBeforeOperator;
    private Field<?> dateField;

    @BeforeEach
    void setUp() {
        monthsBeforeOperator = new MonthsBeforeOperator();
        dateField = DSL.field("end_date");
    }

    @Test
    @DisplayName("applyToField_givenPositiveMonths_shouldReturnMonthsBeforeCondition")
    void applyToField_givenPositiveMonths_shouldReturnMonthsBeforeCondition() {
        BigDecimal monthsValue = new BigDecimal("3");

        Condition condition = monthsBeforeOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusMonths(3);
        assertTrue(sql.contains("end_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("applyToField_givenZeroMonths_shouldReturnTodayCondition")
    void applyToField_givenZeroMonths_shouldReturnTodayCondition() {
        BigDecimal monthsValue = new BigDecimal("0");

        Condition condition = monthsBeforeOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now();
        assertTrue(sql.contains("end_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
    }

    @Test
    @DisplayName("applyToField_givenSingleMonth_shouldReturnMonthBeforeCondition")
    void applyToField_givenSingleMonth_shouldReturnMonthBeforeCondition() {
        BigDecimal monthsValue = new BigDecimal("1");

        Condition condition = monthsBeforeOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusMonths(1);
        assertTrue(sql.contains("end_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("applyToField_givenLargeMonths_shouldReturnDistantPastCondition")
    void applyToField_givenLargeMonths_shouldReturnDistantPastCondition() {
        BigDecimal monthsValue = new BigDecimal("12");

        Condition condition = monthsBeforeOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusMonths(12);
        assertTrue(sql.contains("end_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("applyToField_givenDecimalMonths_shouldReturnMonthsBeforeCondition")
    void applyToField_givenDecimalMonths_shouldReturnMonthsBeforeCondition() {
        BigDecimal monthsValue = new BigDecimal("6.0");

        Condition condition = monthsBeforeOperator.applyToField(dateField, monthsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusMonths(6);
        assertTrue(sql.contains("end_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }
}
