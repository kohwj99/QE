package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.StartsWithOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartsWithOperatorTest {

    private StartsWithOperator operator;

    @BeforeEach
    void setUp() {
        operator = new StartsWithOperator();
    }

    @Test
    void apply_givenValidStringValue_shouldReturnLikeCondition() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);
        String value = "Ali";

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().toLowerCase().contains("like"));
        assertTrue(condition.toString().contains("Ali%"));
    }

    @Test
    void apply_givenEmptyStringValue_shouldReturnLikeConditionWithOnlyWildcard() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);
        String value = "";

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        assertTrue(condition.toString().contains("%"));
    }

    @Test
    void apply_givenNullValue_shouldThrowInvalidQueryException() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);

        // Act + Assert
        assertThrows(InvalidQueryException.class, () -> operator.apply(field, null));
    }

    @Test
    void apply_givenNonStringValue_shouldThrowInvalidQueryException() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);
        Object value = 123; // Not a String

        // Act + Assert
        assertThrows(InvalidQueryException.class, () -> operator.apply(field, value));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> operator.apply(null, "son"));
    }
}
