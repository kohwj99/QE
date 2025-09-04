package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.LessThanOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LessThanOperatorTest {

    private LessThanOperator operator;

    @BeforeEach
    void setUp() {
        operator = new LessThanOperator();
    }

    @Test
    void apply_givenBigDecimalFieldAndValue_shouldReturnLessThanCondition() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);
        BigDecimal value = new BigDecimal("5000");

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("<"));
        assertTrue(condition.toString().contains("5000"));
    }

    @Test
    void apply_givenLocalDateFieldAndValue_shouldReturnLessThanCondition() {
        // Arrange
        Field<LocalDate> field = DSL.field("created_date", LocalDate.class);
        LocalDate value = LocalDate.of(2025, 1, 1);

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("<"));
        assertTrue(condition.toString().contains("2025-01-01"));
    }

    @Test
    void apply_givenNullValue_shouldThrowInvalidQueryException() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);

        // Act + Assert
        assertThrows(InvalidQueryException.class, () -> operator.apply(field, null));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        // Arrange
        Field<?> field = null;

        // Act + Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, BigDecimal.ONE));
    }

    @Test
    void apply_givenZeroBigDecimalValue_shouldReturnLessThanCondition() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);
        BigDecimal value = BigDecimal.ZERO;

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("<"));
        assertTrue(condition.toString().contains("0"));
    }

    @Test
    void apply_givenPastLocalDateValue_shouldReturnLessThanCondition() {
        // Arrange
        Field<LocalDate> field = DSL.field("created_date", LocalDate.class);
        LocalDate value = LocalDate.of(2000, 1, 1);

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("<"));
        assertTrue(condition.toString().contains("2000-01-01"));
    }
}
