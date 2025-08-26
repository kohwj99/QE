package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.LessThanOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LessThanOperator Unit Tests")
class LessThanOperatorUnitTest {

    private LessThanOperator<BigDecimal> lessThanOperator;
    private Field<BigDecimal> decimalField;
    private Field<LocalDate> dateField;
    private Field<BigDecimal> integerLikeField;

    @BeforeEach
    void setUp() {
        lessThanOperator = new LessThanOperator<>();
        decimalField = DSL.field("price", BigDecimal.class);
        dateField = DSL.field("created_date", LocalDate.class);
        integerLikeField = DSL.field("age", BigDecimal.class);
    }

    @Test
    @DisplayName("apply_givenBigDecimalValue_shouldReturnLessThanCondition")
    void apply_givenBigDecimalValue_shouldReturnLessThanCondition() {
        BigDecimal value = new BigDecimal("100.50");

        Condition condition = lessThanOperator.apply(decimalField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("price"), "SQL should contain field name");
        assertTrue(sql.contains("<") || sql.contains("lt"), "SQL should contain less than operator");
        assertTrue(sql.contains("100.50"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenLocalDateValue_shouldReturnLessThanCondition")
    void apply_givenLocalDateValue_shouldReturnLessThanCondition() {
        LessThanOperator dateOperator = new LessThanOperator();
        LocalDate value = LocalDate.of(2023, 12, 31);

        Condition condition = dateOperator.apply(dateField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains("<") || sql.contains("lt"), "SQL should contain less than operator");
        assertTrue(sql.contains("2023-12-31"), "SQL should contain the date value");
    }

    @Test
    @DisplayName("apply_givenIntegerLikeValue_shouldReturnLessThanCondition")
    void apply_givenIntegerLikeValue_shouldReturnLessThanCondition() {
        BigDecimal value = new BigDecimal("25");

        Condition condition = lessThanOperator.apply(integerLikeField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("age"), "SQL should contain field name");
        assertTrue(sql.contains("<") || sql.contains("lt"), "SQL should contain less than operator");
        assertTrue(sql.contains("25"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnLessThanCondition")
    void apply_givenZeroValue_shouldReturnLessThanCondition() {
        BigDecimal value = BigDecimal.ZERO;

        Condition condition = lessThanOperator.apply(decimalField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("price"), "SQL should contain field name");
        assertTrue(sql.contains("<") || sql.contains("lt"), "SQL should contain less than operator");
        assertTrue(sql.contains("0"), "SQL should contain zero value");
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnLessThanCondition")
    void apply_givenNegativeValue_shouldReturnLessThanCondition() {
        BigDecimal value = new BigDecimal("-50.25");

        Condition condition = lessThanOperator.apply(decimalField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("price"), "SQL should contain field name");
        assertTrue(sql.contains("<") || sql.contains("lt"), "SQL should contain less than operator");
        assertTrue(sql.contains("-50.25"), "SQL should contain the negative value");
    }
}
