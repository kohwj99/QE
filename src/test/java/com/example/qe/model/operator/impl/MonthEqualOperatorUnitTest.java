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

@DisplayName("MonthEqualOperator Unit Tests")
class MonthEqualOperatorUnitTest {

    private MonthEqualOperator monthEqualOperator;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        monthEqualOperator = new MonthEqualOperator();
        dateField = DSL.field("birth_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenJanuaryMonth_shouldReturnMonthEqualCondition")
    void apply_givenJanuaryMonth_shouldReturnMonthEqualCondition() {
        Integer month = 1;

        Condition condition = monthEqualOperator.apply(dateField, BigDecimal.valueOf(month));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("1") || sql.toLowerCase().contains("month"), "SQL should reference month 1");
    }

    @Test
    @DisplayName("apply_givenDecemberMonth_shouldReturnMonthEqualCondition")
    void apply_givenDecemberMonth_shouldReturnMonthEqualCondition() {
        Integer month = 12;

        Condition condition = monthEqualOperator.apply(dateField, BigDecimal.valueOf(month));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("=") || sql.contains("eq"), "SQL should contain equals operator");
        assertTrue(sql.contains("12") || sql.toLowerCase().contains("month"), "SQL should reference month 12");
    }

    @Test
    @DisplayName("apply_givenMiddleMonth_shouldReturnMonthEqualCondition")
    void apply_givenMiddleMonth_shouldReturnMonthEqualCondition() {
        Integer month = 6;

        Condition condition = monthEqualOperator.apply(dateField, BigDecimal.valueOf(month));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("birth_date"), "SQL should contain field name");
        assertTrue(sql.contains("6") || sql.toLowerCase().contains("month"), "SQL should reference month 6");
    }

    @Test
    @DisplayName("apply_givenDifferentFieldName_shouldReturnMonthEqualCondition")
    void apply_givenDifferentFieldName_shouldReturnMonthEqualCondition() {
        Field<LocalDate> customField = DSL.field("event_date", LocalDate.class);
        Integer month = 3;

        Condition condition = monthEqualOperator.apply(customField, BigDecimal.valueOf(month));

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("event_date"), "SQL should contain custom field name");
    }
}
