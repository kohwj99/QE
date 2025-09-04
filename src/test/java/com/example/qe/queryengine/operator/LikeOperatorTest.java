package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.operator.impl.LikeOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LikeOperatorTest {

    private LikeOperator operator;
    private Field<String> field;

    @BeforeEach
    void setup() {
        // Arrange: create operator and field
        operator = new LikeOperator();
        field = DSL.field("name", String.class);
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    @Test
    void apply_givenValidStringValue_shouldReturnLikeCondition() {
        // Arrange
        String value = "Alice";

        // Act
        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        // Assert
        assertTrue(sql.contains("namelike'%alice%'"), "SQL should contain LIKE condition");
    }

    @Test
    void apply_givenEmptyString_shouldReturnLikeCondition() {
        // Arrange
        String value = "";

        // Act
        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        // Assert
        assertTrue(sql.contains("namelike'%%'"), "SQL should handle empty string");
    }

    @Test
    void apply_givenNonStringValue_shouldThrowIllegalArgumentException() {
        // Arrange
        Integer value = 123;

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> operator.apply(field, value));
        assertTrue(ex.getMessage().contains("expects a String value"));
    }

    @Test
    void apply_givenNullValue_shouldThrowIllegalArgumentException() {
        // Arrange
        Object value = null;

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> operator.apply(field, value));
        assertTrue(ex.getMessage().contains("expects a String value"));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        // Arrange
        Field<String> nullField = null;
        String value = "%Alice%";

        // Act & Assert
        assertThrows(NullPointerException.class, () -> operator.apply(nullField, value));
    }
}

