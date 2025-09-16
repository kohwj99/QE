package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.DayOfWeekOperator;
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

class DayOfWeekOperatorTest {

    private DayOfWeekOperator operator;
    private Field<LocalDate> validField;
    private String dateStr;

    @BeforeEach
    void setup() {
        operator = new DayOfWeekOperator();
        validField = DSL.field("date_field", LocalDate.class);
        LocalDate date = LocalDate.now();
        dateStr = date.toString();
    }

    private void assertSqlContainsExpectedDay(Condition condition, int expectedDay) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);

        String normalized = sql.replaceAll("\\s+", "").toLowerCase();

        assertTrue(normalized.contains("%7"),
                "Expected SQL to contain modulo, but got: " + normalized);

        assertTrue(normalized.contains("=" + expectedDay),
                "Expected SQL to compare to " + expectedDay + ", but got: " + normalized);
    }

    // ✅ Positive: all weekdays (Monday=1 to Sunday=7)
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    void apply_givenValidDay_shouldGenerateCorrectSQL(int day) {
        BigDecimal value = BigDecimal.valueOf(day);
        Condition condition = operator.apply(validField, value);
        assertSqlContainsExpectedDay(condition, day);
    }

    // ✅ Edge values still generate SQL
    @ParameterizedTest
    @ValueSource(ints = {0, 8, -1, 100})
    void apply_givenEdgeOrInvalidDay_shouldStillGenerateSQL(int day) {
        BigDecimal value = BigDecimal.valueOf(day);
        Condition condition = operator.apply(validField, value);
        assertSqlContainsExpectedDay(condition, day);
    }

    // ❌ Null value
    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
        Exception ex = assertThrows(InvalidQueryException.class,
                () -> operator.apply(validField, null));
        assertEquals("Day value cannot be null", ex.getMessage());
    }

    // ❌ Invalid field types
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

    // ❌ Invalid value types
    @Test
    void apply_givenStringValue_shouldThrowClassCastException() {
        assertThrows(ClassCastException.class,
                () -> operator.apply(validField, "Monday"));
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

    // -------------------- EVALUATE METHOD TESTS --------------------

    @Test
    void evaluate_givenMatchingDay_shouldReturnTrueCondition() {
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        Condition condition = operator.evaluate(dateStr, BigDecimal.valueOf(dayOfWeek));
        assertEquals("(1 = 1)", condition.toString());
    }

    @Test
    void evaluate_givenNonMatchingDay_shouldReturnFalseCondition() {
        LocalDate today = LocalDate.now();
        today = today.plusDays(1);
        int dayOfWeek = today.getDayOfWeek().getValue();
        Condition condition = operator.evaluate(dateStr, BigDecimal.valueOf(dayOfWeek));
        assertEquals("(1 = 0)", condition.toString());
    }
}
