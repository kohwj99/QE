package com.example.qe.sample;

import com.example.qe.queryengine.operator.GenericOperator;
import com.example.qe.queryengine.operator.OperatorFactory;
import com.example.qe.queryengine.operator.OperatorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class OperatorFactoryTest {

    private OperatorFactory factory;
    private OperatorRegistry registry;

    private GenericOperator equalsOperator;
    private GenericOperator dayEqualOperator;

    @BeforeEach
    void setUp() {
        registry = Mockito.mock(OperatorRegistry.class);
        factory = new OperatorFactory(registry);

        equalsOperator = Mockito.mock(GenericOperator.class);
        dayEqualOperator = Mockito.mock(GenericOperator.class);

        // Mock registry.get for realistic operator scenarios
        Mockito.when(registry.get(eq("equals"), eq(String.class), eq(String.class))).thenReturn(equalsOperator);
        Mockito.when(registry.get(eq("equals"), eq(BigDecimal.class), eq(BigDecimal.class))).thenReturn(equalsOperator);
        Mockito.when(registry.get(eq("dayEqual"), eq(LocalDate.class), eq(BigDecimal.class))).thenReturn(dayEqualOperator);

        // Mock registry.getSupportedValueTypes
        Mockito.when(registry.getSupportedValueTypes(eq("equals")))
                .thenReturn(Set.of(String.class, BigDecimal.class));
        Mockito.when(registry.getSupportedValueTypes(eq("dayEqual")))
                .thenReturn(Set.of(BigDecimal.class));
        Mockito.when(registry.getSupportedValueTypes(eq("unknown")))
                .thenReturn(Set.of());
    }

    @Test
    void resolve_withValidInputs_returnsOperator() {
        assertEquals(equalsOperator, factory.resolve("equals", String.class, String.class));
        assertEquals(equalsOperator, factory.resolve("equals", BigDecimal.class, BigDecimal.class));
        assertEquals(dayEqualOperator, factory.resolve("dayEqual", LocalDate.class, BigDecimal.class));
    }

    @Test
    void resolve_withUnsupportedTypes_throwsException() {
        Mockito.when(registry.get(eq("equals"), eq(String.class), eq(LocalDate.class))).thenReturn(null);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                factory.resolve("equals", String.class, LocalDate.class));
        assertTrue(ex.getMessage().contains("does not support"));
    }

    @Test
    void resolve_withUnknownName_throwsException() {
        Mockito.when(registry.get(eq("unknown"), any(), any())).thenReturn(null);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                factory.resolve("unknown", String.class, String.class));
        assertTrue(ex.getMessage().contains("Operator unknown"));
    }

    @Test
    void resolveValueType_returnsCorrectType() {
        assertEquals(String.class, factory.resolveValueType("equals", String.class));
        assertEquals(BigDecimal.class, factory.resolveValueType("equals", BigDecimal.class));
        assertEquals(BigDecimal.class, factory.resolveValueType("dayEqual", BigDecimal.class));
    }

    @Test
    void resolveValueType_returnsFirstTypeIfNotSupported() {
        assertEquals(String.class, factory.resolveValueType("equals", Integer.class));
    }

    @Test
    void resolveValueType_withUnknownOperator_throwsException() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                factory.resolveValueType("unknown", String.class));
        assertTrue(ex.getMessage().contains("No value types registered"));
    }
}