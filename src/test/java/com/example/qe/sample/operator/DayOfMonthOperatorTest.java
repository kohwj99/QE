package com.example.qe.sample.operator;

import com.example.qe.queryengine.operator.impl.DayOfMonthOperator;
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

class DayOfMonthOperatorTest {

    private DayOfMonthOperator operator;
    private Field<LocalDate> validField;

    @BeforeEach
    void setup() {
        operator = new DayOfMonthOperator();
        validField = DSL.field("date_field", LocalDate.class);
    }

    private void assertSqlContainsExpectedDay(Condition condition, int expectedDay) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);

        String normalized = sql.replaceAll("\\s+", "").toLowerCase();

        // Accept ANSI SQL extraction syntax
        assertTrue(normalized.contains("extract(dayfrom"),
                "Expected SQL to contain day extraction, but got: " + normalized);

        assertTrue(normalized.contains("=" + expectedDay),
                "Expected SQL to compare to " + expectedDay + ", but got: " + normalized);
    }


    // ✅ Positive: valid days 1 to 31
    @ParameterizedTest
    @ValueSource(ints = {1, 15, 31})
    void apply_givenValidDay_shouldGenerateCorrectSQL(int day) {
        BigDecimal value = BigDecimal.valueOf(day);
        Condition condition = operator.apply(validField, value);
        assertSqlContainsExpectedDay(condition, day);
    }

    // ✅ Edge cases: days outside 1–31 (still generates SQL, validation is left to DB)
    @ParameterizedTest
    @ValueSource(ints = {0, 32, -1, 100})
    void apply_givenEdgeDay_shouldStillGenerateSQL(int day) {
        BigDecimal value = BigDecimal.valueOf(day);
        Condition condition = operator.apply(validField, value);
        assertSqlContainsExpectedDay(condition, day);
    }

    // ❌ Null value
    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> operator.apply(validField, null));
        assertEquals("Day value cannot be null", ex.getMessage());
    }

    // ❌ Invalid field types
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

    // ❌ Invalid value types
    @Test
    void apply_givenStringValue_shouldThrowClassCastException() {
        assertThrows(ClassCastException.class,
                () -> operator.apply(validField, "15"));
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
