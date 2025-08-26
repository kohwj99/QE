package com.example.qe.model.operator.impl;

import com.example.qe.queryengine.operator.impl.GreaterThanOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GreaterThanOperator Unit Tests")
class GreaterThanOperatorUnitTest {

    private GreaterThanOperator greaterThanOperator;
    private Field<BigDecimal> numericField;
    private Field<Integer> integerField;

    @BeforeEach
    void setUp() {
        greaterThanOperator = new GreaterThanOperator();
        numericField = DSL.field("price", BigDecimal.class);
        integerField = DSL.field("quantity", Integer.class);
    }

    @Test
    @DisplayName("apply_givenBigDecimalValue_shouldReturnGreaterThanCondition")
    void apply_givenBigDecimalValue_shouldReturnGreaterThanCondition() {
        BigDecimal value = new BigDecimal("100.50");

        Condition condition = greaterThanOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("price"), "SQL should contain field name");
        assertTrue(sql.contains(">") || sql.contains("gt"), "SQL should contain greater than operator");
        assertTrue(sql.contains("100.5"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenIntegerValue_shouldReturnGreaterThanCondition")
    void apply_givenIntegerValue_shouldReturnGreaterThanCondition() {
        Integer value = 10;

        Condition condition = greaterThanOperator.apply(integerField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("quantity"), "SQL should contain field name");
        assertTrue(sql.contains(">") || sql.contains("gt"), "SQL should contain greater than operator");
        assertTrue(sql.contains("10"), "SQL should contain the value");
    }

    @Test
    @DisplayName("apply_givenZeroValue_shouldReturnGreaterThanCondition")
    void apply_givenZeroValue_shouldReturnGreaterThanCondition() {
        BigDecimal value = BigDecimal.ZERO;

        Condition condition = greaterThanOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("price"), "SQL should contain field name");
        assertTrue(sql.contains(">") || sql.contains("gt"), "SQL should contain greater than operator");
        assertTrue(sql.contains("0"), "SQL should contain zero value");
    }

    @Test
    @DisplayName("apply_givenNegativeValue_shouldReturnGreaterThanCondition")
    void apply_givenNegativeValue_shouldReturnGreaterThanCondition() {
        BigDecimal value = new BigDecimal("-50.25");

        Condition condition = greaterThanOperator.apply(numericField, value);

        assertNotNull(condition, "Condition should not be null");
        String sql = condition.toString();
        assertTrue(sql.contains("price"), "SQL should contain field name");
        assertTrue(sql.contains(">") || sql.contains("gt"), "SQL should contain greater than operator");
    }
}
