package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("YearsBeforeOperator Unit Tests")
class YearsBeforeOperatorUnitTest {

    private YearsBeforeOperator yearsBeforeOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        yearsBeforeOperator = new YearsBeforeOperator();
        dateField = DSL.field("created_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenPositiveYears_shouldReturnYearsBeforeCondition")
    void apply_givenPositiveYears_shouldReturnYearsBeforeCondition() {
        Integer yearsValue = 5;

        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusYears(5);
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("apply_givenZeroYears_shouldReturnTodayCondition")
    void apply_givenZeroYears_shouldReturnTodayCondition() {
        Integer yearsValue = 0;

        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
    }

    @Test
    @DisplayName("apply_givenSingleYear_shouldReturnYearBeforeCondition")
    void apply_givenSingleYear_shouldReturnYearBeforeCondition() {
        Integer yearsValue = 1;

        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusYears(1);
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }

    @Test
    @DisplayName("apply_givenLargeYears_shouldReturnDistantPastCondition")
    void apply_givenLargeYears_shouldReturnDistantPastCondition() {
        Integer yearsValue = 100;

        Condition condition = yearsBeforeOperator.apply(dateField, yearsValue);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusYears(100);
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
    }
}
