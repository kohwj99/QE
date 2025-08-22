package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DayEqualOperator Unit Tests")
class DayEqualOperatorUnitTest {

    private DayEqualOperator dayEqualOperator;
    private Field<?> dateField;

    @BeforeEach
    void setUp() {
        dayEqualOperator = new DayEqualOperator();
        dateField = DSL.field("birth_date");
    }

    @Test
    @DisplayName("applyToField_givenValidDay_shouldReturnDayEqualCondition")
    void applyToField_givenValidDay_shouldReturnDayEqualCondition() {
        BigDecimal dayValue = new BigDecimal("15");

        Condition condition = dayEqualOperator.applyToField(dateField, dayValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("15"), "SQL should contain the day value");
    }

    @Test
    @DisplayName("applyToField_givenFirstDayOfMonth_shouldReturnDayEqualCondition")
    void applyToField_givenFirstDayOfMonth_shouldReturnDayEqualCondition() {
        BigDecimal dayValue = new BigDecimal("1");

        Condition condition = dayEqualOperator.applyToField(dateField, dayValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("1"), "SQL should contain the day value");
    }

    @Test
    @DisplayName("applyToField_givenLastDayOfMonth_shouldReturnDayEqualCondition")
    void applyToField_givenLastDayOfMonth_shouldReturnDayEqualCondition() {
        BigDecimal dayValue = new BigDecimal("31");

        Condition condition = dayEqualOperator.applyToField(dateField, dayValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("31"), "SQL should contain the day value");
    }

    @Test
    @DisplayName("applyToField_givenMidMonthDay_shouldReturnDayEqualCondition")
    void applyToField_givenMidMonthDay_shouldReturnDayEqualCondition() {
        BigDecimal dayValue = new BigDecimal("25");

        Condition condition = dayEqualOperator.applyToField(dateField, dayValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("25"), "SQL should contain the day value");
    }

    @Test
    @DisplayName("applyToField_givenZeroDay_shouldReturnDayEqualCondition")
    void applyToField_givenZeroDay_shouldReturnDayEqualCondition() {
        BigDecimal dayValue = new BigDecimal("0");

        Condition condition = dayEqualOperator.applyToField(dateField, dayValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("0"), "SQL should contain the day value");
    }
}
