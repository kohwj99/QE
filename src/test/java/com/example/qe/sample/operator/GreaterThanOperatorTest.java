package com.example.qe.sample.operator;

import com.example.qe.queryengine.operator.impl.GreaterThanOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GreaterThanOperatorTest {

    private GreaterThanOperator operator;

    @BeforeEach
    void setUp() {
        operator = new GreaterThanOperator();
    }

    @Test
    void apply_givenBigDecimalFieldAndValue_shouldReturnGreaterThanCondition() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);
        BigDecimal value = new BigDecimal("5000");

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().toLowerCase().contains(">"));
        assertTrue(condition.toString().contains("5000"));
    }

    @Test
    void apply_givenLocalDateFieldAndValue_shouldReturnGreaterThanCondition() {
        // Arrange
        Field<LocalDate> field = DSL.field("created_date", LocalDate.class);
        LocalDate value = LocalDate.of(2025, 1, 1);

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().toLowerCase().contains(">"));
        assertTrue(condition.toString().contains("2025-01-01"));
    }

    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, null));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        // Arrange
        Field<?> field = null;

        // Act + Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, BigDecimal.ONE));
    }

    @Test
    void apply_givenZeroBigDecimalValue_shouldReturnGreaterThanCondition() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);
        BigDecimal value = BigDecimal.ZERO;

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("0"));
        assertTrue(condition.toString().contains(">"));
    }

    @Test
    void apply_givenPastLocalDateValue_shouldReturnGreaterThanCondition() {
        // Arrange
        Field<LocalDate> field = DSL.field("created_date", LocalDate.class);
        LocalDate value = LocalDate.of(2000, 1, 1);

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("2000-01-01"));
        assertTrue(condition.toString().contains(">"));
    }
}
