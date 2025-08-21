package com.example.qe.model.operator.impl;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("YearEqualOperator Unit Tests")
class YearEqualOperatorUnitTest {

    private YearEqualOperator yearEqualOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        yearEqualOperator = new YearEqualOperator();
        dateField = DSL.field("hire_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenCurrentYear_shouldReturnYearEqualCondition")
    void apply_givenCurrentYear_shouldReturnYearEqualCondition() {
        Integer year = 2025;

        Condition condition = yearEqualOperator.apply(dateField, year);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("hire_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("2025") || sql.toLowerCase().contains("year"), "SQL should reference year 2025");
    }

    @Test
    @DisplayName("apply_givenPastYear_shouldReturnYearEqualCondition")
    void apply_givenPastYear_shouldReturnYearEqualCondition() {
        Integer year = 2020;

        Condition condition = yearEqualOperator.apply(dateField, year);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("hire_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("2020") || sql.toLowerCase().contains("year"), "SQL should reference year 2020");
    }

    @Test
    @DisplayName("apply_givenFutureYear_shouldReturnYearEqualCondition")
    void apply_givenFutureYear_shouldReturnYearEqualCondition() {
        Integer year = 2030;

        Condition condition = yearEqualOperator.apply(dateField, year);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("hire_date"), "SQL should contain field name");
        assertTrue(sql.contains("2030") || sql.toLowerCase().contains("year"), "SQL should reference year 2030");
    }

    @Test
    @DisplayName("apply_givenDifferentFieldName_shouldReturnYearEqualCondition")
    void apply_givenDifferentFieldName_shouldReturnYearEqualCondition() {
        Field<LocalDate> customField = DSL.field("graduation_date", LocalDate.class);
        Integer year = 2023;

        Condition condition = yearEqualOperator.apply(customField, year);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("graduation_date"), "SQL should contain custom field name");
    }
}
