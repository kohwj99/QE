package com.example.qe.util;

import com.example.qe.model.operator.Operator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OperatorFactory Unit Tests")
class OperatorFactoryUnitTest {

    private OperatorFactory operatorFactory;
    private OperatorRegistry operatorRegistry;

    @BeforeEach
    void setUp() {
        operatorRegistry = new OperatorRegistry();
        OperatorScanner scanner = new OperatorScanner(operatorRegistry);
        scanner.scanAndRegister("com.example.qe.model.operator");
        operatorFactory = new OperatorFactory(operatorRegistry);
    }

    @Test
    @DisplayName("resolve_givenValidStringEqualsOperator_shouldReturnOperator")
    void resolve_givenValidStringEqualsOperator_shouldReturnOperator() {
        Operator<String, String> operator = (Operator<String, String>) operatorFactory.resolve("equals", String.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("EqualsOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalEqualsOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalEqualsOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("equals", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("EqualsOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidStringLikeOperator_shouldReturnOperator")
    void resolve_givenValidStringLikeOperator_shouldReturnOperator() {
        Operator<String, String> operator = (Operator<String, String>) operatorFactory.resolve("like", String.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("LikeOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidStringStartsWithOperator_shouldReturnOperator")
    void resolve_givenValidStringStartsWithOperator_shouldReturnOperator() {
        Operator<String, String> operator = (Operator<String, String>) operatorFactory.resolve("startsWith", String.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("StartsWithOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidStringEndsWithOperator_shouldReturnOperator")
    void resolve_givenValidStringEndsWithOperator_shouldReturnOperator() {
        Operator<String, String> operator = (Operator<String, String>) operatorFactory.resolve("endsWith", String.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("EndsWithOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalGreaterThanOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalGreaterThanOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("greaterThan", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("GreaterThanOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalLessThanOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalLessThanOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("lessThan", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("LessThanOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalDayEqualOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalDayEqualOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("dayEqual", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("DayEqualOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalMonthEqualOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalMonthEqualOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("monthEqual", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("MonthEqualOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalYearEqualOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalYearEqualOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("yearEqual", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("YearEqualOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalDaysAfterOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalDaysAfterOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("daysAfter", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("DaysAfterOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBigDecimalDaysBeforeOperator_shouldReturnOperator")
    void resolve_givenValidBigDecimalDaysBeforeOperator_shouldReturnOperator() {
        Operator<BigDecimal, BigDecimal> operator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("daysBefore", BigDecimal.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("DaysBeforeOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidStringIsNullOperator_shouldReturnOperator")
    void resolve_givenValidStringIsNullOperator_shouldReturnOperator() {
        Operator<String, String> operator = (Operator<String, String>) operatorFactory.resolve("isNull", String.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("IsNullOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidStringIsNotNullOperator_shouldReturnOperator")
    void resolve_givenValidStringIsNotNullOperator_shouldReturnOperator() {
        Operator<String, String> operator = (Operator<String, String>) operatorFactory.resolve("isNotNull", String.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("IsNotNullOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenInvalidOperatorName_shouldThrowIllegalArgumentException")
    void resolve_givenInvalidOperatorName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("invalidOperator", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found for name: invalidOperator"));
        assertTrue(exception.getMessage().contains("and type: java.lang.String"));
    }

    @Test
    @DisplayName("resolve_givenNullOperatorName_shouldThrowIllegalArgumentException")
    void resolve_givenNullOperatorName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve(null, String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found for name: null"));
    }

    @Test
    @DisplayName("resolve_givenEmptyOperatorName_shouldThrowIllegalArgumentException")
    void resolve_givenEmptyOperatorName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found for name:"));
    }

    @Test
    @DisplayName("resolve_givenLikeOperatorWithUnsupportedLocalDateType_shouldThrowIllegalArgumentException")
    void resolve_givenLikeOperatorWithUnsupportedLocalDateType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("like", LocalDate.class);
        });

        // Print actual message for debugging
        System.out.println("Actual error message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenStartsWithOperatorWithUnsupportedBigDecimalType_shouldThrowIllegalArgumentException")
    void resolve_givenStartsWithOperatorWithUnsupportedBigDecimalType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("startsWith", BigDecimal.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenEndsWithOperatorWithUnsupportedLocalDateType_shouldThrowIllegalArgumentException")
    void resolve_givenEndsWithOperatorWithUnsupportedLocalDateType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("endsWith", LocalDate.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenDayEqualOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException")
    void resolve_givenDayEqualOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("dayEqual", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenMonthEqualOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException")
    void resolve_givenMonthEqualOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("monthEqual", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenYearEqualOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException")
    void resolve_givenYearEqualOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("yearEqual", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenDaysAfterOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException")
    void resolve_givenDaysAfterOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("daysAfter", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenGreaterThanOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException")
    void resolve_givenGreaterThanOperatorWithUnsupportedStringType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("greaterThan", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found") ||
                   exception.getMessage().contains("does not support type"),
                   "Should throw exception for unsupported type combination");
    }

    @Test
    @DisplayName("resolve_givenValidBooleanEqualsOperator_shouldReturnOperator")
    void resolve_givenValidBooleanEqualsOperator_shouldReturnOperator() {
        Operator<Boolean, Boolean> operator = (Operator<Boolean, Boolean>) operatorFactory.resolve("equals", Boolean.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("EqualsOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidBooleanNotEqualsOperator_shouldReturnOperator")
    void resolve_givenValidBooleanNotEqualsOperator_shouldReturnOperator() {
        Operator<Boolean, Boolean> operator = (Operator<Boolean, Boolean>) operatorFactory.resolve("notEquals", Boolean.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("NotEqualsOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidLocalDateEqualsOperator_shouldReturnOperator")
    void resolve_givenValidLocalDateEqualsOperator_shouldReturnOperator() {
        Operator<LocalDate, LocalDate> operator = (Operator<LocalDate, LocalDate>) operatorFactory.resolve("equals", LocalDate.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("EqualsOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidLocalDateGreaterThanOperator_shouldReturnOperator")
    void resolve_givenValidLocalDateGreaterThanOperator_shouldReturnOperator() {
        Operator<LocalDate, LocalDate> operator = (Operator<LocalDate, LocalDate>) operatorFactory.resolve("greaterThan", LocalDate.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("GreaterThanOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenValidLocalDateLessThanOperator_shouldReturnOperator")
    void resolve_givenValidLocalDateLessThanOperator_shouldReturnOperator() {
        Operator<LocalDate, LocalDate> operator = (Operator<LocalDate, LocalDate>) operatorFactory.resolve("lessThan", LocalDate.class);

        assertNotNull(operator, "Operator should not be null");
        assertEquals("LessThanOperator", operator.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenCaseSensitiveOperatorName_shouldThrowIllegalArgumentException")
    void resolve_givenCaseSensitiveOperatorName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("EQUALS", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found for name: EQUALS"));
    }

    @Test
    @DisplayName("resolve_givenWhitespaceOperatorName_shouldThrowIllegalArgumentException")
    void resolve_givenWhitespaceOperatorName_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("   ", String.class);
        });

        assertTrue(exception.getMessage().contains("No operator found for name:    "));
    }

    @Test
    @DisplayName("resolve_givenValidOperatorMultipleTimes_shouldReturnSameOperatorType")
    void resolve_givenValidOperatorMultipleTimes_shouldReturnSameOperatorType() {
        Operator<String, String> operator1 = (Operator<String, String>) operatorFactory.resolve("equals", String.class);
        Operator<String, String> operator2 = (Operator<String, String>) operatorFactory.resolve("equals", String.class);

        assertNotNull(operator1, "First operator should not be null");
        assertNotNull(operator2, "Second operator should not be null");
        assertEquals(operator1.getClass(), operator2.getClass(), "Both operators should be of same type");
        assertEquals("EqualsOperator", operator1.getClass().getSimpleName());
        assertEquals("EqualsOperator", operator2.getClass().getSimpleName());
    }

    @Test
    @DisplayName("resolve_givenMixedValidAndInvalidCalls_shouldHandleAppropriately")
    void resolve_givenMixedValidAndInvalidCalls_shouldHandleAppropriately() {
        // Valid call should succeed
        Operator<String, String> validOperator = (Operator<String, String>) operatorFactory.resolve("equals", String.class);
        assertNotNull(validOperator, "Valid operator should not be null");

        // Invalid call should fail
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operatorFactory.resolve("invalidOp", String.class);
        });
        assertTrue(exception.getMessage().contains("No operator found"));

        // Another valid call should succeed after the invalid one
        Operator<BigDecimal, BigDecimal> anotherValidOperator = (Operator<BigDecimal, BigDecimal>) operatorFactory.resolve("greaterThan", BigDecimal.class);
        assertNotNull(anotherValidOperator, "Another valid operator should not be null");
    }
}
