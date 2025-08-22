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

@DisplayName("DaysAfterOperator Unit Tests")
class DaysAfterOperatorUnitTest {

    private DaysAfterOperator daysAfterOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        daysAfterOperator = new DaysAfterOperator();
        dateField = DSL.field("due_date", LocalDate.class);
    }

    @Test
    @DisplayName("applyToField_givenPositiveDays_shouldReturnDaysAfterCondition")
    void applyToField_givenPositiveDays_shouldReturnDaysAfterCondition() {
        Integer days = 14;
        LocalDate expectedDate = LocalDate.now().plusDays(14);

        Condition condition = daysAfterOperator.applyToField(dateField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("due_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain calculated date");
    }

    @Test
    @DisplayName("apply_givenZeroDays_shouldReturnDaysAfterCondition")
    void apply_givenZeroDays_shouldReturnDaysAfterCondition() {
        Integer days = 0;
        LocalDate expectedDate = LocalDate.now();

        Condition condition = daysAfterOperator.applyToField(dateField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("due_date"), "SQL should contain field name");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
    }

    @Test
    @DisplayName("apply_givenLargeNumberOfDays_shouldReturnDaysAfterCondition")
    void apply_givenLargeNumberOfDays_shouldReturnDaysAfterCondition() {
        Integer days = 90;
        LocalDate expectedDate = LocalDate.now().plusDays(90);

        Condition condition = daysAfterOperator.applyToField(dateField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("due_date"), "SQL should contain field name");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain calculated future date");
    }

    @Test
    @DisplayName("apply_givenDifferentFieldName_shouldReturnDaysAfterCondition")
    void apply_givenDifferentFieldName_shouldReturnDaysAfterCondition() {
        Field<LocalDate> customField = DSL.field("scheduled_date", LocalDate.class);
        Integer days = 7;

        Condition condition = daysAfterOperator.applyToField(customField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("scheduled_date"), "SQL should contain custom field name");
    }
}
