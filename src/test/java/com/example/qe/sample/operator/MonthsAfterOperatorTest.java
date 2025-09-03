package com.example.qe.sample.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.MonthsAfterOperator;
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

class MonthsAfterOperatorTest {

    private MonthsAfterOperator operator;
    private Field<LocalDate> validField;

    @BeforeEach
    void setup() {
        operator = new MonthsAfterOperator();
        validField = DSL.field("date_field", LocalDate.class);
    }

    private String renderSql(Condition condition) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);
        return sql.replaceAll("\\s+", "").toLowerCase();
    }

    // --- Positive cases: valid month values ---
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 12})
    void apply_givenValidMonths_shouldGenerateExpectedSQL(int months) {
        BigDecimal value = BigDecimal.valueOf(months);

        Condition condition = operator.apply(validField, value);
        String sql = renderSql(condition);

        LocalDate expectedDate = LocalDate.now().minusMonths(months);
        assertTrue(sql.contains(expectedDate.toString()),
                "Expected SQL to contain " + expectedDate + ", but got: " + sql);
        assertTrue(sql.contains("cast(date_fieldasdate)=date"),
                "Expected SQL to compare to field, but got: " + sql);
    }

    // --- Null value ---
    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
        Exception ex = assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, null));
        assertEquals("Month value cannot be null", ex.getMessage());
    }

    // --- Invalid field types ---
    @Test
    void apply_givenStringField_shouldThrowIllegalArgumentException() {
        Field<String> stringField = DSL.field("string_field", String.class);
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(stringField, BigDecimal.ONE));
    }

    @Test
    void apply_givenBigDecimalField_shouldThrowIllegalArgumentException() {
        Field<BigDecimal> decimalField = DSL.field("decimal_field", BigDecimal.class);
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(decimalField, BigDecimal.ONE));
    }

    @Test
    void apply_givenBooleanField_shouldThrowIllegalArgumentException() {
        Field<Boolean> booleanField = DSL.field("bool_field", Boolean.class);
        assertThrows(InvalidQueryException.class,
                () -> operator.apply(booleanField, BigDecimal.ONE));
    }

    // --- Invalid value types ---
    @Test
    void apply_givenStringValue_shouldThrowClassCastException() {
        assertThrows(ClassCastException.class,
                () -> operator.apply(validField, "5"));
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
