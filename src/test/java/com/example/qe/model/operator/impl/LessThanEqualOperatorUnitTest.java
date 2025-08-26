package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.LessThanEqualOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LessThanEqualOperator Unit Tests")
class LessThanEqualOperatorUnitTest {

    private LessThanEqualOperator<BigDecimal> lessThanEqualOperator;
    private Field<BigDecimal> numericField;
    private Field<BigDecimal> integerLikeField;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        lessThanEqualOperator = new LessThanEqualOperator<>();
        numericField = DSL.field("budget", BigDecimal.class);
        integerLikeField = DSL.field("max_items", BigDecimal.class);
        dateField = DSL.field("created_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenBigDecimalValue_shouldReturnLessThanEqualCondition")
    void apply_givenBigDecimalValue_shouldReturnLessThanEqualCondition() {
        BigDecimal value = new BigDecimal("1000.00");

        Condition condition = lessThanEqualOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("budget"), "SQL should contain field name");
        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain less than equal operator");
        assertTrue(sql.contains("1000"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenIntegerLikeValue_shouldReturnLessThanEqualCondition")
    void apply_givenIntegerLikeValue_shouldReturnLessThanEqualCondition() {
        BigDecimal value = new BigDecimal("50");

        Condition condition = lessThanEqualOperator.apply(integerLikeField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("max_items"), "SQL should contain field name");
        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain less than equal operator");
        assertTrue(sql.contains("50"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenLocalDateValue_shouldReturnLessThanEqualCondition")
    void apply_givenLocalDateValue_shouldReturnLessThanEqualCondition() {
        LessThanEqualOperator dateOperator = new LessThanEqualOperator();
        LocalDate value = LocalDate.of(2023, 12, 31);

        Condition condition = dateOperator.apply(dateField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain less than equal operator");
        assertTrue(sql.contains("2023-12-31"), "SQL should contain the date value");
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnLessThanEqualCondition")
    void apply_givenZeroValue_shouldReturnLessThanEqualCondition() {
        BigDecimal value = BigDecimal.ZERO;

        Condition condition = lessThanEqualOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("budget"), "SQL should contain field name");
        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain less than equal operator");
        assertTrue(sql.contains("0"), "SQL should contain zero value");
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnLessThanEqualCondition")
    void apply_givenNegativeValue_shouldReturnLessThanEqualCondition() {
        BigDecimal value = new BigDecimal("-50.25");

        Condition condition = lessThanEqualOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("budget"), "SQL should contain field name");
        assertTrue(sql.contains("<=") || sql.contains("le"), "SQL should contain less than equal operator");
        assertTrue(sql.contains("-50.25"), "SQL should contain the negative value");
    }
}
