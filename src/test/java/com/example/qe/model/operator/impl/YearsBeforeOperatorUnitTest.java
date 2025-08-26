package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.YearsBeforeOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    @DisplayName("applyToField_givenPositiveYears_shouldReturnYearsBeforeCondition")
    void applyToField_givenPositiveYears_shouldReturnYearsBeforeCondition() {
        Integer yearsValue = 5;

        Condition condition = yearsBeforeOperator.applyToField(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusYears(5);
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
        System.out.println(sql);
    }

    @Test
    @DisplayName("applyToField_givenZeroYears_shouldReturnTodayCondition")
    void applyToField_givenZeroYears_shouldReturnTodayCondition() {
        Integer yearsValue = 0;

        Condition condition = yearsBeforeOperator.applyToField(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
        System.out.println(sql);
    }

    @Test
    @DisplayName("applyToField_givenSingleYear_shouldReturnYearBeforeCondition")
    void applyToField_givenSingleYear_shouldReturnYearBeforeCondition() {
        Integer yearsValue = 1;

        Condition condition = yearsBeforeOperator.applyToField(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusYears(1);
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
        System.out.println(sql);
    }

    @Test
    @DisplayName("applyToField_givenLargeYears_shouldReturnDistantPastCondition")
    void applyToField_givenLargeYears_shouldReturnDistantPastCondition() {
        Integer yearsValue = 100;

        Condition condition = yearsBeforeOperator.applyToField(dateField, BigDecimal.valueOf(yearsValue));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        LocalDate expectedDate = LocalDate.now().minusYears(100);
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain the calculated date");
        System.out.println(sql);
    }
}
