package com.example.qe.sample.operator;

import com.example.qe.queryengine.operator.impl.IsNotNullOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IsNotNullOperatorTest {

    private IsNotNullOperator operator;

    @BeforeEach
    void setUp() {
        operator = new IsNotNullOperator();
    }

    @Test
    void apply_givenStringField_shouldReturnNotNullCondition() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);

        // Act
        Condition condition = operator.apply(field, null);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("is not null"));
    }

    @Test
    void apply_givenBigDecimalField_shouldReturnNotNullCondition() {
        // Arrange
        Field<BigDecimal> field = DSL.field("salary", BigDecimal.class);

        // Act
        Condition condition = operator.apply(field, null);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("is not null"));
    }

    @Test
    void apply_givenBooleanField_shouldReturnNotNullCondition() {
        // Arrange
        Field<Boolean> field = DSL.field("active", Boolean.class);

        // Act
        Condition condition = operator.apply(field, null);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("is not null"));
    }

    @Test
    void apply_givenLocalDateField_shouldReturnNotNullCondition() {
        // Arrange
        Field<LocalDate> field = DSL.field("created_date", LocalDate.class);

        // Act
        Condition condition = operator.apply(field, null);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("is not null"));
    }

    @Test
    void apply_givenNonNullValue_shouldIgnoreValueAndReturnNotNullCondition() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);
        String value = "ignored";

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("is not null"));
    }

    @Test
    void apply_givenMockField_shouldCallIsNotNull() {
        // Arrange
        @SuppressWarnings("unchecked")
        Field<String> mockField = mock(Field.class);
        Condition mockCondition = DSL.trueCondition();
        when(mockField.isNotNull()).thenReturn(mockCondition);

        // Act
        Condition result = operator.apply(mockField, null);

        // Assert
        assertEquals(mockCondition, result);
        verify(mockField, times(1)).isNotNull();
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        // Arrange
        Field<?> field = null;

        // Act + Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, null));
    }
}
