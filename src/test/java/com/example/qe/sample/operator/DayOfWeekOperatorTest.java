package com.example.qe.sample.operator;

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
    private Field<LocalDate> field;

    @BeforeEach
    void setup() {
        operator = new DayOfWeekOperator();
        field = DSL.field("date_field", LocalDate.class);
    }

    /**
     * Helper to assert that the SQL contains modulo logic and compares to the expected day.
     * Logs the SQL for visual inspection.
     */
    private void assertSqlContainsExpectedDay(Condition condition, int expectedDay) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);

        String normalized = sql.replaceAll("\\s+", "").toLowerCase();

        // MSSQL uses %7 for modulo, check for that
        assertTrue(normalized.contains("%7"),
                "Expected SQL to contain modulo operation, but got: " + normalized);

        // Check that the condition compares to the expected day
        assertTrue(normalized.contains("=" + expectedDay),
                "Expected SQL to compare to " + expectedDay + ", but got: " + normalized);
    }

    // --- Parameterized positive tests for all weekdays (Monday=1 to Sunday=7) ---
    @ParameterizedTest(name = "apply_givenValidDay{0}_shouldGenerateCorrectSQL")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    void apply_givenValidDay_shouldGenerateCorrectSQL(int day) {
        // Arrange
        BigDecimal value = BigDecimal.valueOf(day);

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertSqlContainsExpectedDay(condition, day);
    }

    // --- Edge / negative day values ---
    @ParameterizedTest(name = "apply_givenEdgeOrInvalidDay{0}_shouldGenerateSQL")
    @ValueSource(ints = {0, 8, -1, 100})
    void apply_givenEdgeOrInvalidDay_shouldGenerateSQL(int day) {
        // Arrange
        BigDecimal value = BigDecimal.valueOf(day);

        // Act
        Condition condition = operator.apply(field, value);

        // Assert
        assertSqlContainsExpectedDay(condition, day);
    }

    // --- Wrong type should throw ClassCastException ---
    @ParameterizedTest(name = "apply_givenNonBigDecimal_{0}_shouldThrowClassCastException")
    @ValueSource(strings = {"Monday", "1", "", "true"})
    void apply_givenNonBigDecimal_shouldThrowClassCastException(String invalidValue) {
        assertThrows(ClassCastException.class, () -> operator.apply(field, invalidValue));
    }

    @Test
    void apply_givenNullValue_shouldThrowNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> operator.apply(field, null));
        assertEquals("Day value cannot be null", ex.getMessage());
    }

}
