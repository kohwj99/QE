package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DayOfMonthOperator Unit Tests")
class DayOfMonthOperatorUnitTest {

    private DayOfMonthOperator dayOfMonthOperator;
    private Field<?> dateField;

    @BeforeEach
    void setUp() {
        dayOfMonthOperator = new DayOfMonthOperator();
        dateField = DSL.field("event_date");
    }

    @Test
    @DisplayName("applyToField_givenValidDayOfMonth_shouldReturnDayOfMonthCondition")
    void applyToField_givenValidDayOfMonth_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("10");

        Condition condition = dayOfMonthOperator.applyToField(dateField, dayOfMonthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("10"), "SQL should contain the day of month value");
    }

    @Test
    @DisplayName("applyToField_givenFirstDayOfMonth_shouldReturnDayOfMonthCondition")
    void applyToField_givenFirstDayOfMonth_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("1");

        Condition condition = dayOfMonthOperator.applyToField(dateField, dayOfMonthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("1"), "SQL should contain the day of month value");
    }

    @Test
    @DisplayName("applyToField_givenLastDayOfMonth_shouldReturnDayOfMonthCondition")
    void applyToField_givenLastDayOfMonth_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("31");

        Condition condition = dayOfMonthOperator.applyToField(dateField, dayOfMonthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("31"), "SQL should contain the day of month value");
    }

    @Test
    @DisplayName("applyToField_givenMidMonthDay_shouldReturnDayOfMonthCondition")
    void applyToField_givenMidMonthDay_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("15");

        Condition condition = dayOfMonthOperator.applyToField(dateField, dayOfMonthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("15"), "SQL should contain the day of month value");
    }

    @Test
    @DisplayName("applyToField_givenEndOfFebruary_shouldReturnDayOfMonthCondition")
    void applyToField_givenEndOfFebruary_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("28");

        Condition condition = dayOfMonthOperator.applyToField(dateField, dayOfMonthValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("28"), "SQL should contain the day of month value");
    }
}
