package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.GreaterThanEqualOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GreaterThanEqualOperator Unit Tests")
class GreaterThanEqualOperatorUnitTest {

    private GreaterThanEqualOperator<BigDecimal> greaterThanEqualOperator;
    private Field<BigDecimal> numericField;
    private Field<BigDecimal> integerLikeField;
    private Field<LocalDate> dateField;

    @BeforeEach
    void setUp() {
        greaterThanEqualOperator = new GreaterThanEqualOperator<>();
        numericField = DSL.field("salary", BigDecimal.class);
        integerLikeField = DSL.field("age", BigDecimal.class);
        dateField = DSL.field("created_date", LocalDate.class);
    }

    @Test
    @DisplayName("apply_givenBigDecimalValue_shouldReturnGreaterThanEqualCondition")
    void apply_givenBigDecimalValue_shouldReturnGreaterThanEqualCondition() {
        BigDecimal value = new BigDecimal("50000.00");

        Condition condition = greaterThanEqualOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("salary"), "SQL should contain field name");
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain greater than equal operator");
        assertTrue(sql.contains("50000"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenIntegerLikeValue_shouldReturnGreaterThanEqualCondition")
    void apply_givenIntegerLikeValue_shouldReturnGreaterThanEqualCondition() {
        BigDecimal value = new BigDecimal("18");

        Condition condition = greaterThanEqualOperator.apply(integerLikeField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("age"), "SQL should contain field name");
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain greater than equal operator");
        assertTrue(sql.contains("18"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnGreaterThanEqualCondition")
    void apply_givenZeroValue_shouldReturnGreaterThanEqualCondition() {
        BigDecimal value = BigDecimal.ZERO;

        Condition condition = greaterThanEqualOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("salary"), "SQL should contain field name");
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain greater than equal operator");
        assertTrue(sql.contains("0"), "SQL should contain zero value");
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnGreaterThanEqualCondition")
    void apply_givenNegativeValue_shouldReturnGreaterThanEqualCondition() {
        BigDecimal value = new BigDecimal("-100.50");

        Condition condition = greaterThanEqualOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("salary"), "SQL should contain field name");
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain greater than equal operator");
        assertTrue(sql.contains("-100.50"), "SQL should contain the negative value");
    }

    @Test
    @DisplayName("apply_givenLocalDateValue_shouldReturnGreaterThanEqualCondition")
    void apply_givenLocalDateValue_shouldReturnGreaterThanEqualCondition() {
        GreaterThanEqualOperator dateOperator = new GreaterThanEqualOperator();
        LocalDate value = LocalDate.of(2023, 1, 1);

        Condition condition = dateOperator.apply(dateField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain greater than equal operator");
        assertTrue(sql.contains("2023-01-01"), "SQL should contain the date value");
    }

    @Test
    @DisplayName("apply_givenCurrentDate_shouldReturnGreaterThanEqualCondition")
    void apply_givenCurrentDate_shouldReturnGreaterThanEqualCondition() {
        GreaterThanEqualOperator dateOperator = new GreaterThanEqualOperator();
        LocalDate value = LocalDate.now();

        Condition condition = dateOperator.apply(dateField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("created_date"), "SQL should contain field name");
        assertTrue(sql.contains(">=") || sql.contains("ge"), "SQL should contain greater than equal operator");
        assertTrue(sql.contains(value.toString()), "SQL should contain today's date");
    }
}
