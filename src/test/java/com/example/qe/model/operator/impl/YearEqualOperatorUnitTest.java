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
    @DisplayName("applyToField_givenCurrentYear_shouldReturnYearEqualCondition")
    void applyToField_givenCurrentYear_shouldReturnYearEqualCondition() {
        Integer year = 2025;
        
        Condition condition = yearEqualOperator.applyToField(dateField, BigDecimal.valueOf(year));
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("hire_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("2025") || sql.toLowerCase().contains("year"), "SQL should reference year 2025");
        System.out.println(sql);
    }

    @Test
    @DisplayName("applyToField_givenPastYear_shouldReturnYearEqualCondition")
    void applyToField_givenPastYear_shouldReturnYearEqualCondition() {
        Integer year = 2020;
        
        Condition condition = yearEqualOperator.applyToField(dateField, BigDecimal.valueOf(year));
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("hire_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("2020") || sql.toLowerCase().contains("year"), "SQL should reference year 2020");
        System.out.println(sql);
    }

    @Test
    @DisplayName("applyToField_givenFutureYear_shouldReturnYearEqualCondition")
    void applyToField_givenFutureYear_shouldReturnYearEqualCondition() {
        Integer year = 2030;
        
        Condition condition = yearEqualOperator.applyToField(dateField, BigDecimal.valueOf(year));
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("hire_date"), "SQL should contain field name");
        assertTrue(sql.contains("2030") || sql.toLowerCase().contains("year"), "SQL should reference year 2030");
        System.out.println(sql);
    }

    @Test
    @DisplayName("applyToField_givenDifferentFieldName_shouldReturnYearEqualCondition")
    void applyToField_givenDifferentFieldName_shouldReturnYearEqualCondition() {
        Field<LocalDate> customField = DSL.field("graduation_date", LocalDate.class);
        Integer year = 2023;
        
        Condition condition = yearEqualOperator.applyToField(customField, BigDecimal.valueOf(year));
        
        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("graduation_date"), "SQL should contain custom field name");
        System.out.println(sql);
    }
}
