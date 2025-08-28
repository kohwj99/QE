package com.example.qe.sample;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OperatorFactoryTest {

    private OperatorFactory factory;
    private OperatorRegistry registry;
    private GenericOperator equalsOperator;
    private GenericOperator dayOfWeekOperator;

    @BeforeEach
    void setup() {
        // Arrange: Mock registry and operators
        registry = mock(OperatorRegistry.class);
        factory = new OperatorFactory(registry);

        equalsOperator = mock(GenericOperator.class);
        dayOfWeekOperator = mock(GenericOperator.class);

        // Mock registry.get for valid operator scenarios
        when(registry.get(eq("equals"), eq(String.class), eq(String.class))).thenReturn(equalsOperator);
        when(registry.get(eq("equals"), eq(BigDecimal.class), eq(BigDecimal.class))).thenReturn(equalsOperator);
        when(registry.get(eq("dayOfWeek"), eq(LocalDate.class), eq(BigDecimal.class))).thenReturn(dayOfWeekOperator);

        // Mock registry.getSupportedValueTypes
        when(registry.getSupportedValueTypes(eq("equals"))).thenReturn(Set.of(String.class, BigDecimal.class));
        when(registry.getSupportedValueTypes(eq("dayOfWeek"))).thenReturn(Set.of(BigDecimal.class));
        when(registry.getSupportedValueTypes(eq("unknown"))).thenReturn(Set.of());
    }

    @Test
    void resolve_givenValidInputs_shouldReturnOperator() {
        // Act & Assert
        assertEquals(equalsOperator, factory.resolve("equals", String.class, String.class));
        assertEquals(equalsOperator, factory.resolve("equals", BigDecimal.class, BigDecimal.class));
        assertEquals(dayOfWeekOperator, factory.resolve("dayOfWeek", LocalDate.class, BigDecimal.class));
    }

    @Test
    void resolve_givenUnsupportedTypes_shouldThrowException() {
        // Arrange
        when(registry.get(eq("equals"), eq(String.class), eq(LocalDate.class))).thenReturn(null);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> factory.resolve("equals", String.class, LocalDate.class));
        assertTrue(ex.getMessage().contains("does not support"));
    }

    @Test
    void resolve_givenUnknownOperatorName_shouldThrowException() {
        // Arrange
        when(registry.get(eq("unknown"), any(), any())).thenReturn(null);

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> factory.resolve("unknown", String.class, String.class));
        assertTrue(ex.getMessage().contains("Operator unknown"));
    }

    @Test
    void resolveValueType_givenSupportedFieldType_shouldReturnSameType() {
        // Act & Assert
        assertEquals(String.class, factory.resolveValueType("equals", String.class));
        assertEquals(BigDecimal.class, factory.resolveValueType("equals", BigDecimal.class));
        assertEquals(BigDecimal.class, factory.resolveValueType("dayOfWeek", BigDecimal.class));
    }

    @Test
    void resolveValueType_givenUnsupportedFieldType_shouldReturnSomeSupportedType() {
        // Act
        Class<?> resolved = factory.resolveValueType("equals", Integer.class);

        // Assert: resolved type must be one of the supported value types
        Set<Class<?>> supported = registry.getSupportedValueTypes("equals");
        assertTrue(supported.contains(resolved), "Resolved type should be in the supported value types");
    }

    @Test
    void resolveValueType_givenUnknownOperator_shouldThrowException() {
        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> factory.resolveValueType("unknown", String.class));
        assertTrue(ex.getMessage().contains("No value types registered"));
    }
}
