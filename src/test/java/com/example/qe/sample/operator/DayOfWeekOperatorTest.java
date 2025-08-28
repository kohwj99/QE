package com.example.qe.sample.operator;

import com.example.qe.queryengine.operator.impl.DayOfWeekOperator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    private void assertSqlContainsExpectedDay(Condition condition, int expectedDay) {
        String sql = DSL.using(SQLDialect.DEFAULT).renderInlined(condition);
        System.out.println("Generated SQL: " + sql);

        String normalized = sql.replaceAll("\\s+", "").toLowerCase();

        assertTrue(normalized.contains("extract(day_of_week") || normalized.contains("dayofweek"),
                "Expected SQL to contain day-of-week extraction, but got: " + normalized);
        assertTrue(normalized.contains("mod("),
                "Expected SQL to contain modulo operation, but got: " + normalized);
        assertTrue(normalized.contains("=" + expectedDay),
                "Expected SQL to compare to " + expectedDay + ", but got: " + normalized);
    }

    @Test
    void apply_givenMonday_shouldGenerateCorrectSQL() {
        // Arrange
        BigDecimal monday = BigDecimal.valueOf(1);

        // Act
        Condition condition = operator.apply(field, monday);
        System.out.println(condition);

        // Assert
        assertSqlContainsExpectedDay(condition, 1);
    }

    @Test
    void apply_givenSunday_shouldGenerateCorrectSQL() {
        // Arrange
        BigDecimal sunday = BigDecimal.valueOf(7);

        // Act
        Condition condition = operator.apply(field, sunday);

        // Assert
        assertSqlContainsExpectedDay(condition, 7);
    }

    @Test
    void apply_givenInvalidDay_shouldGenerateCorrectSQL() {
        // Arrange
        BigDecimal invalid = BigDecimal.valueOf(0);

        // Act
        Condition condition = operator.apply(field, invalid);

        // Assert
        assertSqlContainsExpectedDay(condition, 0);
    }

    @Test
    void apply_givenNonBigDecimal_shouldThrowClassCastException() {
        // Arrange
        Object invalid = "Monday";

        // Act & Assert
        assertThrows(ClassCastException.class, () -> operator.apply(field, invalid));
    }

    @Test
    void apply_givenNullDay_shouldThrowNullPointerException() {
        // Arrange
        Object nullDay = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> operator.apply(field, nullDay));
    }
}
