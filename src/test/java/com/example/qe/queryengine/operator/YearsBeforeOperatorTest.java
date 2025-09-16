package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.exception.InvalidQueryException;
import com.example.qe.queryengine.operator.impl.YearsBeforeOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YearsBeforeOperatorTest {

    private YearsBeforeOperator operator;
    private Field<LocalDate> validField;
    private String dateStr;

    @BeforeEach
    void setup() {
        operator = new YearsBeforeOperator();
        validField = DSL.field("date_field", LocalDate.class);
        LocalDate date = LocalDate.now();
        date = date.plusYears(1);
        dateStr = date.toString();
    }

    // --- Positive cases ---
    @Test
    void apply_givenValidYearOffset_shouldGenerateExpectedDateCondition() {
        BigDecimal yearsOffset = BigDecimal.valueOf(5);
        LocalDate expectedDate = LocalDate.now().plusYears(5);

        Condition condition = operator.apply(validField, yearsOffset);
        String sql = condition.toString();

        assertTrue(sql.contains(expectedDate.toString()),
                "Expected SQL to reference date " + expectedDate + ", got: " + sql);
    }

    // --- Null value ---
    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
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


    // -------------------- EVALUATE METHOD TESTS --------------------

    @Test
    void evaluate_givenMatchingYears_shouldReturnTrueCondition() {
        BigDecimal years = BigDecimal.valueOf(1);
        Condition condition = operator.evaluate(dateStr, years);
        assertEquals("(1 = 1)", condition.toString());
    }

    @Test
    void evaluate_givenNonMatchingYears_shouldReturnFalseCondition() {
        BigDecimal years = BigDecimal.valueOf(5);
        Condition condition = operator.evaluate(dateStr, years);
        assertEquals("(1 = 0)", condition.toString());
    }
}
