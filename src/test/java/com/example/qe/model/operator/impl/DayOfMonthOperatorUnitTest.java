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

@DisplayName("DayOfMonthOperator Unit Tests")
class DayOfMonthOperatorUnitTest {

    private DayOfMonthOperator dayOfMonthOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        dayOfMonthOperator = new DayOfMonthOperator();
        dateField = DSL.field("event_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenValidDayOfMonth_shouldReturnDayOfMonthCondition")
    void apply_givenValidDayOfMonth_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("10");
        Condition condition = dayOfMonthOperator.apply(dateField, dayOfMonthValue);
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("10"), "SQL should contain the day of month value");
    }

    @Test
    @DisplayName("apply_givenFirstDayOfMonth_shouldReturnDayOfMonthCondition")
    void apply_givenFirstDayOfMonth_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("1");
        Condition condition = dayOfMonthOperator.apply(dateField, dayOfMonthValue);
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("1"), "SQL should contain the day of month value");
    }

    @Test
    @DisplayName("apply_givenLastDayOfMonth_shouldReturnDayOfMonthCondition")
    void apply_givenLastDayOfMonth_shouldReturnDayOfMonthCondition() {
        BigDecimal dayOfMonthValue = new BigDecimal("31");
        Condition condition = dayOfMonthOperator.apply(dateField, dayOfMonthValue);
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain field name");
        assertTrue(sql.contains("31"), "SQL should contain the day of month value");
    }
}
