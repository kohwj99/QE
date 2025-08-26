package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.DaysBeforeOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DaysBeforeOperator Unit Tests")
class DaysBeforeOperatorUnitTest {

    private DaysBeforeOperator daysBeforeOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        daysBeforeOperator = new DaysBeforeOperator();
        dateField = DSL.field("created_date", LocalDate.class);
    }

    @Test
    @DisplayName("applyToField_givenPositiveDays_shouldReturnDaysBeforeCondition")
    void applyToField_givenPositiveDays_shouldReturnDaysBeforeCondition() {
        Integer days = 7;
        LocalDate expectedDate = LocalDate.now().minusDays(7);

        Condition condition = daysBeforeOperator.applyToField(dateField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain calculated date");
    }

    @Test
    @DisplayName("applyToField_givenZeroDays_shouldReturnDaysBeforeCondition")
    void applyToField_givenZeroDays_shouldReturnDaysBeforeCondition() {
        Integer days = 0;
        LocalDate expectedDate = LocalDate.now();

        Condition condition = daysBeforeOperator.applyToField(dateField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain today's date");
    }

    @Test
    @DisplayName("applyToField_givenLargeNumberOfDays_shouldReturnDaysBeforeCondition")
    void applyToField_givenLargeNumberOfDays_shouldReturnDaysBeforeCondition() {
        Integer days = 365;
        LocalDate expectedDate = LocalDate.now().minusDays(365);

        Condition condition = daysBeforeOperator.applyToField(dateField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains(expectedDate.toString()), "SQL should contain calculated date one year ago");
    }

    @Test
    @DisplayName("applyToField_givenDifferentFieldName_shouldReturnDaysBeforeCondition")
    void applyToField_givenDifferentFieldName_shouldReturnDaysBeforeCondition() {
        Field<LocalDate> customField = DSL.field("updated_at", LocalDate.class);
        Integer days = 30;

        Condition condition = daysBeforeOperator.applyToField(customField, BigDecimal.valueOf(days));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("updated_at"), "SQL should contain custom field name");
    }
}
