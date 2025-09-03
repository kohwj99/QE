package com.example.qe.sample.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.YearEqualOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YearEqualOperatorTest {

    private YearEqualOperator operator;
    private Field<LocalDate> validField;

    @BeforeEach
    void setup() {
        operator = new YearEqualOperator();
        validField = DSL.field("date_field", LocalDate.class);
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    // --- Positive cases for sample years ---
    @ParameterizedTest
    @ValueSource(ints = {1999, 2000, 2024, 2050})
    void apply_givenValidYear_shouldGenerateExpectedSQL(int year) {
        BigDecimal value = BigDecimal.valueOf(year);

        Condition condition = operator.apply(validField, value);
        String sql = renderSql(condition);

        assertTrue(sql.contains("year(cast(date_fieldasdate))=" + year),
                "Expected SQL to contain year extraction comparing to " + year + ", got: " + sql);
    }

    // --- Null value ---
    @Test
    void apply_givenNullValue_shouldThrowInvalidQueryException() {
        Exception ex = assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, null));
        assertEquals("Year value cannot be null", ex.getMessage());
    }

    // --- Invalid field types ---
    @Test
    void apply_givenStringField_shouldThrowInvalidQueryException() {
        Field<String> stringField = DSL.field("string_field", String.class);
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(stringField, BigDecimal.ONE));
    }

    @Test
    void apply_givenBigDecimalField_shouldThrowInvalidQueryException() {
        Field<BigDecimal> decimalField = DSL.field("decimal_field", BigDecimal.class);
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(decimalField, BigDecimal.ONE));
    }

    @Test
    void apply_givenBooleanField_shouldThrowInvalidQueryException() {
        Field<Boolean> booleanField = DSL.field("bool_field", Boolean.class);
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(booleanField, BigDecimal.ONE));
    }

    // --- Invalid value types ---
    @Test
    void apply_givenStringValue_shouldThrowClassCastException() {
        assertThrows(ClassCastException.class,
                () -> operator.apply(validField, "2024"));
    }

    @Test
    void apply_givenLocalDateValue_shouldThrowClassCastException() {
        assertThrows(ClassCastException.class,
                () -> operator.apply(validField, LocalDate.now()));
    }

    @Test
    void apply_givenBooleanValue_shouldThrowClassCastException() {
        assertThrows(ClassCastException.class,
                () -> operator.apply(validField, true));
    }
}
