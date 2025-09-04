package com.example.qe.queryengine.operator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OperatorRegistryTest {

    private OperatorRegistry registry;
    private GenericOperator equalsOperator;
    private GenericOperator dayEqualOperator;

    @BeforeEach
    void setUp() {
        registry = new OperatorRegistry();
        equalsOperator = Mockito.mock(GenericOperator.class);
        dayEqualOperator = Mockito.mock(GenericOperator.class);
    }

    @Test
    void register_and_getOperator_returnsCorrectInstance() {
        registry.register("equals", new Class[]{String.class, BigDecimal.class}, new Class[]{String.class, BigDecimal.class}, equalsOperator);
        registry.register("dayEqual", new Class[]{LocalDate.class}, new Class[]{BigDecimal.class}, dayEqualOperator);

        assertEquals(equalsOperator, registry.getOperator("equals"));
        assertEquals(dayEqualOperator, registry.getOperator("dayEqual"));
    }

    @Test
    void get_returnsOperatorForSupportedTypes() {
        registry.register("equals", new Class[]{String.class}, new Class[]{String.class}, equalsOperator);
        registry.register("dayEqual", new Class[]{LocalDate.class}, new Class[]{BigDecimal.class}, dayEqualOperator);

        assertEquals(equalsOperator, registry.get("equals", String.class, String.class));
        assertEquals(dayEqualOperator, registry.get("dayEqual", LocalDate.class, BigDecimal.class));
    }

    @Test
    void get_returnsNullForUnsupportedTypes() {
        registry.register("equals", new Class[]{String.class}, new Class[]{String.class}, equalsOperator);

        assertNull(registry.get("equals", LocalDate.class, String.class));
        assertNull(registry.get("equals", String.class, LocalDate.class));
        assertNull(registry.get("equals", BigDecimal.class, BigDecimal.class));
    }

    @Test
    void get_withNullArguments_returnsNull() {
        assertNull(registry.get(null, null, null));
    }

    @Test
    void getSupportedValueTypes_returnsAllRegisteredTypes() {
        registry.register("equals", new Class[]{String.class, BigDecimal.class}, new Class[]{String.class, BigDecimal.class}, equalsOperator);
        registry.register("dayEqual", new Class[]{LocalDate.class}, new Class[]{BigDecimal.class}, dayEqualOperator);

        Set<Class<?>> valueTypes = registry.getSupportedValueTypes("equals");
        assertTrue(valueTypes.contains(String.class));
        assertTrue(valueTypes.contains(BigDecimal.class));
    }

    @Test
    void getSupportedValueTypes_returnsEmptySetForUnknownOperator() {
        assertTrue(registry.getSupportedValueTypes("unknown").isEmpty());
    }

    @Test
    void getOperator_returnsNullForUnregisteredName() {
        assertNull(registry.getOperator("notRegistered"));
    }
}