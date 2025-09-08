package com.example.qe.queryengine.operator;

import com.example.qe.queryengine.operator.impl.EqualsOperator;
import com.example.qe.queryengine.operator.impl.DayOfWeekOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OperatorScannerTest {

    private OperatorRegistry registry;
    private OperatorScanner scanner;

    @BeforeEach
    void setup() {
        // Arrange
        registry = mock(OperatorRegistry.class);
        scanner = new OperatorScanner(registry);
    }

    @Test
    void scanAndRegister_givenPackageWithOperators_shouldRegisterEqualsOperator() {
        // Act
        scanner.scanAndRegister();

        // Assert
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Class<?>[]> fieldTypesCaptor = (ArgumentCaptor<Class<?>[]>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(Class[].class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Class<?>[]> valueTypesCaptor = (ArgumentCaptor<Class<?>[]>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(Class[].class);
        ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);

        verify(registry, atLeastOnce()).register(
                nameCaptor.capture(),
                fieldTypesCaptor.capture(),
                valueTypesCaptor.capture(),
                (GenericOperator) instanceCaptor.capture()
        );

        assertTrue(nameCaptor.getAllValues().contains("equals"), "EqualsOperator should be registered");
        assertTrue(instanceCaptor.getAllValues().stream().anyMatch(EqualsOperator.class::isInstance), "EqualsOperator instance should be registered");

        // Verify annotation field and value types
        OperatorAnnotation annotation = EqualsOperator.class.getAnnotation(OperatorAnnotation.class);
        assertArrayEquals(new Class[]{String.class, java.math.BigDecimal.class, Boolean.class, java.time.LocalDate.class},
                annotation.supportedFieldTypes(), "EqualsOperator supportedFieldTypes mismatch");
        assertArrayEquals(new Class[]{String.class, java.math.BigDecimal.class, Boolean.class, java.time.LocalDate.class},
                annotation.supportedValueTypes(), "EqualsOperator supportedValueTypes mismatch");
    }

    @Test
    void scanAndRegister_givenPackageWithOperators_shouldRegisterDayOfWeekOperator() {
        // Act
        scanner.scanAndRegister();

        // Assert
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Class<?>[]> fieldTypesCaptor = (ArgumentCaptor<Class<?>[]>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(Class[].class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Class<?>[]> valueTypesCaptor = (ArgumentCaptor<Class<?>[]>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(Class[].class);
        ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);

        verify(registry, atLeastOnce()).register(
                nameCaptor.capture(),
                fieldTypesCaptor.capture(),
                valueTypesCaptor.capture(),
                (GenericOperator) instanceCaptor.capture()
        );

        assertTrue(nameCaptor.getAllValues().contains("dayOfWeek"), "DayOfWeekOperator should be registered");
        assertTrue(instanceCaptor.getAllValues().stream().anyMatch(DayOfWeekOperator.class::isInstance), "DayOfWeekOperator instance should be registered");

        OperatorAnnotation annotation = DayOfWeekOperator.class.getAnnotation(OperatorAnnotation.class);
        assertArrayEquals(new Class[]{java.time.LocalDate.class},
                annotation.supportedFieldTypes(), "DayOfWeekOperator supportedFieldTypes mismatch");
        assertArrayEquals(new Class[]{java.math.BigDecimal.class},
                annotation.supportedValueTypes(), "DayOfWeekOperator supportedValueTypes mismatch");
    }
}
