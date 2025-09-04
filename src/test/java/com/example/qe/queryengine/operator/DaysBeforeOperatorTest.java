package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.DaysBeforeOperator;
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

class DaysBeforeOperatorTest {

    private DaysBeforeOperator operator;
    private Field<LocalDate> validField;

    @BeforeEach
    void setup() {
        operator = new DaysBeforeOperator();
        validField = DSL.field("date_field", LocalDate.class);
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    // --- Positive cases: valid number of days ---
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 30})
    void apply_givenValidDays_shouldGenerateExpectedSQL(int days) {
        BigDecimal value = BigDecimal.valueOf(days);

        Condition condition = operator.apply(validField, value);
        String sql = renderSql(condition);

        LocalDate expectedDate = LocalDate.now().plusDays(days);
        assertTrue(sql.contains(expectedDate.toString()),
                "Expected SQL to contain " + expectedDate + ", but got: " + sql);
        assertTrue(sql.contains("(cast(date_fieldasdate)="),
                "Expected SQL to compare to field, but got: " + sql);
    }

    // --- Null value ---
    @Test
    void apply_givenNullValue_shouldThrowInvalidQueryException() {
        Exception ex = assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, null));
        assertEquals("Day value cannot be null", ex.getMessage());
    }

    // --- Invalid field types ---
    @Test
    void apply_givenStringField_shouldThrowIllegalArgumentException() {
        Field<String> stringField = DSL.field("string_field", String.class);
        assertThrows(IllegalArgumentException.class,
                () -> operator.apply(stringField, BigDecimal.ONE));
    }

    @Test
    void apply_givenBigDecimalField_shouldThrowIllegalArgumentException() {
        Field<BigDecimal> decimalField = DSL.field("decimal_field", BigDecimal.class);
        assertThrows(IllegalArgumentException.class,
                () -> operator.apply(decimalField, BigDecimal.ONE));
    }

    @Test
    void apply_givenBooleanField_shouldThrowIllegalArgumentException() {
        Field<Boolean> booleanField = DSL.field("bool_field", Boolean.class);
        assertThrows(IllegalArgumentException.class,
                () -> operator.apply(booleanField, BigDecimal.ONE));
    }

    // --- Invalid value types ---
    @Test
    void apply_givenStringValue_shouldThrowInvalidQueryException() {
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, "5"));
    }

    @Test
    void apply_givenLocalDateValue_shouldThrowInvalidQueryException() {
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, LocalDate.now()));
    }

    @Test
    void apply_givenBooleanValue_shouldThrowInvalidQueryException() {
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, true));
    }
}
