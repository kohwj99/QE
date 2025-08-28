package com.example.qe.sample.operator;

import com.example.qe.queryengine.operator.impl.GreaterThanEqualOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GreaterThanEqualOperatorTest {

    private GreaterThanEqualOperator operator;

    @BeforeEach
    void setup() {
        operator = new GreaterThanEqualOperator();
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    @Test
    void apply_givenBigDecimalFieldAndValue_shouldGenerateCorrectSQL() {
        // Arrange
        Field<BigDecimal> field = DSL.field("amount", BigDecimal.class);
        BigDecimal value = BigDecimal.valueOf(100);

        // Act
        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        // Assert
        assertTrue(sql.contains("amount>=100"));
    }

    @Test
    void apply_givenLocalDateFieldAndValue_shouldGenerateCorrectSQL() {
        // Arrange
        Field<LocalDate> field = DSL.field("created", LocalDate.class);
        LocalDate value = LocalDate.of(2024, 6, 1);

        // Act
        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        // Assert
        assertTrue(sql.contains("created>=date'2024-06-01'"));
    }

    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
        // Arrange
        Field<BigDecimal> field = DSL.field("amount", BigDecimal.class);
        Object value = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, value));
    }

    @Test
    void apply_givenNullField_shouldThrowNullPointerException() {
        // Arrange
        Field<BigDecimal> field = null;
        BigDecimal value = BigDecimal.ONE;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, value));
    }

    @Test
    void apply_givenEdgeCaseValue_shouldGenerateCorrectSQL() {
        // Arrange
        Field<BigDecimal> field = DSL.field("amount", BigDecimal.class);
        BigDecimal value = BigDecimal.ZERO;

        // Act
        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        // Assert
        assertTrue(sql.contains("amount>=0"));
    }

    @Test
    void apply_givenPastLocalDate_shouldGenerateCorrectSQL() {
        // Arrange
        Field<LocalDate> field = DSL.field("created", LocalDate.class);
        LocalDate value = LocalDate.of(1999, 1, 1);

        // Act
        Condition condition = operator.apply(field, value);
        String sql = renderSql(condition);

        // Assert
        assertTrue(sql.contains("created>=date'1999-01-01'"));
    }
}
