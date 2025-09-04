package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.EndsWithOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndsWithOperatorTest {

    private EndsWithOperator operator;

    @BeforeEach
    void setUp() {
        operator = new EndsWithOperator();
    }

    @Test
    void apply_givenValidStringValue_shouldReturnEndsWithLikeCondition() {
        // Arrange
        Field<String> field = DSL.field("name", String.class);
        String value = "son";

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertNotNull(condition);
        String conditionStr = condition.toString().toLowerCase();
        assertTrue(conditionStr.contains("like"));
        assertTrue(conditionStr.contains("%" + value));
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
        String conditionStr = condition.toString();
        assertTrue(conditionStr.contains("%"));
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
        Object value = 123;

        // Act + Assert
        assertThrows(InvalidQueryException.class, () -> operator.apply(field, value));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {

        // Act + Assert
        assertThrows(NullPointerException.class, () -> operator.apply(null, "son"));
    }
}
